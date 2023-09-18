package eu.qped.java.checkers.mutation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.coverage.FeedbackMessage;
import eu.qped.java.checkers.coverage.MemoryLoader;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.coverage.framework.coverage.Jacoco;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfCoverageSettings;
import eu.qped.java.checkers.mass.QfMutationSettings;
import eu.qped.java.checkers.mass.ShowFor;

public class MutationChecker<R> implements MutationInterface<R> {

//	private QfCoverageSettings covSettings;

    private File solutionRoot;
    private QfMutationSettings settings;

    public MutationChecker(QfMutationSettings settings, File solutionRoot) {
        this.settings = settings;
        this.solutionRoot = solutionRoot;
    }


    public List<String> check() {
        // Determine the application classes and test classes in the solution
        List<CoverageFacade> testClasses = new ArrayList<>();
        List<CoverageFacade> classes = new ArrayList<>();
        try {
            separateTestAndApplicationClasses(testClasses, classes);
        } catch (IOException e) {
            throw new RuntimeException("Could not create lists of test and application classes.", e);
        }

        // Create a classloader for the student and instructor classes
        MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
        for (CoverageFacade testClass : testClasses) {
            memoryLoader.upload(testClass.className(), testClass.byteCode());
        }
        for (CoverageFacade clazz : classes) {
            memoryLoader.upload(clazz.className(), clazz.byteCode());
        }

        // create a runner for the tests, currently only JUnit 5 supported
        TestFramework test = new JUnit5();

        // List of messages (in Markdown) that will be displayed to students.
        // Leave list empty for no feeback.
        List<String> messages = new ArrayList<>();

        // if the report settings are set to false, show empty message
        if (!settings.getShowFullMutationReport()) {
            return messages;
        }


        List<String> testClassNames = testClasses.stream().map(CoverageFacade::className).collect(Collectors.toList());

        Class<?> MutationInfrastructureClass;
        Class<?> VariantClass;
        List<Pair<?>> resultList = new ArrayList<>();

        try {
            // Create an instance of the loaded class (assuming it has a default constructor)
            MutationInfrastructureClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.MutationInfrastructure");
            VariantClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.Variant");

            // get the list of MutationInfrastructure inside Privateimpl
            resultList = (List<Pair<?>>) MutationInfrastructureClass.getDeclaredMethod("getListOfPairs").invoke(MutationInfrastructureClass.getDeclaredConstructor().newInstance());



        } catch (Exception e) {
            throw new RuntimeException("Could not load class.", e);
        }


        // run all tests
        List<String> testResults = test.testing(testClassNames, memoryLoader);
        if (!testResults.isEmpty()) {
            //if tests fail, show empty message
            return messages;
        }

        for (int j=0;j<resultList.size();j++) {
            System.out.println("MutationList:" + j + "Correct Variant: " + resultList.get(j));
        }

        int mutationPossibilities = calculatePossibilities(resultList);
        // Create an immutable list with 8 true values
        List<Boolean> immutableList = Collections.nCopies(resultList.size(), true);

        // Convert the immutable list to a mutable ArrayList
        List<Boolean> correctVariants = new ArrayList<>(immutableList);
        try  {
            Field field = MutationInfrastructureClass.getDeclaredField("firstTest");
            field.setAccessible(true);
            field.set(null,  false);


            // Create an instance of MutationInfrastructureClass using the no-argument constructor
            Object mutationInfrastructureInstance = MutationInfrastructureClass.getDeclaredConstructor().newInstance();

            // Get the setCorrectVariants method with the correct signature
            MutationInfrastructureClass.getDeclaredMethod("setCorrectVariants", List.class).invoke(mutationInfrastructureInstance, correctVariants);



        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }


        // Loop over variants
        // (replace with proper loop condition
        for (int i = 0; i < mutationPossibilities; i++) {
            // run all tests
        //    List<String> testResults = test.testing(testClassNames, memoryLoader);

            // Iterate through the list, toggling one element to false in each iteration
            for (int k = 0; k < correctVariants.size(); k++) {
                for (int j = 0; j < correctVariants.size(); j++) {
                    correctVariants.set(j, k != j);
                }
                try {
                    // Create an instance of MutationInfrastructureClass using the no-argument constructor
                    Object mutationInfrastructureInstance = MutationInfrastructureClass.getDeclaredConstructor().newInstance();

                    // Get the setCorrectVariants method with the correct signature
                    MutationInfrastructureClass.getDeclaredMethod("setCorrectVariants", List.class).invoke(mutationInfrastructureInstance, correctVariants);

                    System.out.println(MutationInfrastructureClass.getDeclaredMethod("getCorrectVariants").invoke(mutationInfrastructureInstance));

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(correctVariants);
               test.testing(testClassNames,memoryLoader);
            }

            messages.add("### Mutation Coverage Feedback");
            messages.add("- Add the message which you get from the variant");

            // if testResults is empty, all tests succeeded (or there were no tests)
            // depending on the type of variant (is it the correct implementation, or a mutant with a defect)
            // generate appropriate feedback message (i.e., the message configured by the instructor for this case)
            // and add it to 'messages'.


        }
        // end loop


        return messages;
    }

    public void separateTestAndApplicationClasses(List<CoverageFacade> testClasses, List<CoverageFacade> classes)
            throws IOException {
        List<File> allClassFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "class");
        String solutionDirectoryPath = solutionRoot.getCanonicalPath() + File.separator;

        // The file separator will be used as regular expression by replaceAll.
        // Therefore, we must escape the separator on Windows systems.
        String fileSeparator = File.separator;
        if (fileSeparator.equals("\\")) {
            fileSeparator = "\\\\";
        }
        for (File file : allClassFiles) {
            String filename = file.getCanonicalPath();

            String classname = filename.
                    substring(solutionDirectoryPath.length(), filename.length() - ".class".length()).
                    replaceAll(fileSeparator, ".");

            String[] classnameSegments = classname.split("\\.");
            String simpleClassname = classnameSegments[classnameSegments.length - 1];

            CoverageFacade coverageFacade = new CoverageFacade(
                    Files.readAllBytes(file.toPath()),
                    classname);

            if (simpleClassname.startsWith("Test")
                    || simpleClassname.startsWith("test")
                    || simpleClassname.endsWith("Test")
                    || simpleClassname.endsWith("test")) {
                // the class is a test
                testClasses.add(coverageFacade);
            } else {
                // the class is an application class (i.e., no test)
                classes.add(coverageFacade);
            }
        }
    }

    @Override
    public R doit() {
        System.out.println("DID IT");
        return null;
    }

    public int calculatePossibilities(List<Pair<?>> listOfLists) {


        return (int) Math.pow(2,listOfLists.size());
    }

    public void reloadTestingClasses() {

    }
}