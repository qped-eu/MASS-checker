package eu.qped.java.checkers.mutation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.coverage.MemoryLoader;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfMutationSettings;
import eu.qped.racket.functions.numbers.Even;

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

        MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
        loadClassesIntoMemory(testClasses, classes, memoryLoader);

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
System.out.println(classes.stream().map(CoverageFacade::className).collect(Collectors.toList()));
        Class<?> mutationInfrastructureClass;
        Class<?> variantClass;
        Class<?> pairClass;
        List<?> resultList = new ArrayList<>();

        try {
            // Create an instance of the loaded class (assuming it has a default constructor)
            mutationInfrastructureClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.MutationInfrastructure");
            variantClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.Variant");
            pairClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.Pair");

            // get the list of MutationInfrastructure inside Privateimpl
            //     resultList = (List<?>) mutationInfrastructureClass.getDeclaredMethod("getListOfPairs").invoke(mutationInfrastructureClass.getDeclaredConstructor().newInstance());


        } catch (Exception e) {
            throw new RuntimeException("Could not load class.", e);
        }


        // run all tests
        List<String> testResults = test.testing(testClassNames, memoryLoader);

        if (!testResults.isEmpty()) {
            System.out.println("Der erste Test ist fehlgeschlagen" + testResults);
            return messages;
        }

        for (int i = 0; i < 0; i++) {
            try {
                System.out.println(variantClass.getDeclaredMethod("getMsg").invoke(pairClass.getDeclaredMethod("getMutationVariant").invoke(resultList.get(i))));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        int mutationPossibilities;
        try {
            Field fieldMessage = mutationInfrastructureClass.getDeclaredField("mutationMessageList");
            // Make the field accessible if it's not public
            fieldMessage.setAccessible(true);

            // Get the value of the field from the instance
            List<String> message = (List<String>) fieldMessage.get(mutationInfrastructureClass.getDeclaredConstructor().newInstance());

            mutationPossibilities = message.size();
        } catch (NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Mutation possibilities: " + mutationPossibilities);
        for (int i = 0; i < mutationPossibilities; i++) {
            // Iterate through the list, toggling one element to false in each iteration
            List<Boolean> tmpList = new ArrayList<>(Collections.nCopies(mutationPossibilities, true));
            tmpList.set(i, false);
            try {
                // Create a classloader for the student and instructor classes
                memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
                loadClassesIntoMemory(testClasses, classes, memoryLoader);

                // Create an instance of the loaded class (assuming it has a default constructor)
                mutationInfrastructureClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.MutationInfrastructure");
                variantClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.Variant");
                pairClass = memoryLoader.loadClass("eu.qped.java.checkers.mutation.Pair");
                Object mutationInfrastructureInstance;

                Field field = mutationInfrastructureClass.getDeclaredField("firstTest");
                field.setAccessible(true);
                field.set(null, false);

                // get the list of MutationInfrastructure inside Privateimpl
                //      resultList = (List<?>) mutationInfrastructureClass.getDeclaredMethod("getListOfPairs").invoke(mutationInfrastructureClass.getDeclaredConstructor().newInstance());

                // Create an instance of MutationInfrastructureClass using the no-argument constructor
                mutationInfrastructureInstance = mutationInfrastructureClass.getDeclaredConstructor().newInstance();

                // Get the setCorrectVariants method with the correct signature
                mutationInfrastructureClass.getDeclaredMethod("setCorrectVariants", List.class).invoke(mutationInfrastructureInstance, tmpList);



                //System.out.println("OUTSIDE CLASS: " + mutationInfrastructureClass.getDeclaredMethod("getCorrectVariants").invoke(mutationInfrastructureInstance));

                //      System.out.println(mutationInfrastructureClass.getDeclaredMethod("isFirstTest").invoke(mutationInfrastructureInstance));

                List<String> testResultstmp = test.testing(testClassNames, memoryLoader);
                  // System.out.println((Boolean) variantClass.getDeclaredMethod("getActive").invoke(pairClass.getDeclaredMethod("getMutationVariant").invoke(resultList.get(i))));

                // Falls Test nicht fehlschlägt, ist es ein Mutant mit einem Defekt
                if (testResultstmp.isEmpty()) {
                    System.out.println("Wir haben ein Mutant mit der Defektnachricht:");

                    // String message = (String) variantClass.getDeclaredMethod("getMsg").invoke(pairClass.getDeclaredMethod("getMutationVariant").invoke(resultList.get(i)));
                    Field fieldMessage = mutationInfrastructureClass.getDeclaredField("mutationMessageList");
                    // Make the field accessible if it's not public
                    fieldMessage.setAccessible(true);

                    // Get the value of the field from the instance
                    List<String> message = (List<String>) fieldMessage.get(mutationInfrastructureInstance);
                    System.out.println(message.get(i));
                    messages.add(message.get(i));
                    return messages;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     InstantiationException | ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }


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

    private void loadClassesIntoMemory(List<CoverageFacade> testClasses, List<CoverageFacade> classes, MemoryLoader memoryLoader) {
        for (CoverageFacade testClass : testClasses) {
            memoryLoader.upload(testClass.className(), testClass.byteCode());
        }
        for (CoverageFacade clazz : classes) {
            memoryLoader.upload(clazz.className(), clazz.byteCode());
        }
    }

    @Override
    public R doit() {
        System.out.println("DID IT");
        return null;
    }

    public int calculatePossibilities(List<?> listOfLists) {


        return listOfLists.size();
    }

    public void reloadTestingClasses() {

    }
}