package eu.qped.java.checkers.mutation;

import eu.qped.java.checkers.Facade;
import eu.qped.java.checkers.coverage.MemoryLoader;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfMutationSettings;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MutationChecker {
    private final File solutionRoot;
    private final QfMutationSettings settings;

    public MutationChecker(QfMutationSettings settings, File solutionRoot) {
        this.settings = settings;
        this.solutionRoot = solutionRoot;
    }

    public List<String> check() {
        // Determine the application classes and test classes in the solution
        List<Facade> testClasses = new ArrayList<>();
        List<Facade> classes = new ArrayList<>();
        try {
            Facade.separateTestAndApplicationClasses(solutionRoot,testClasses, classes);
        } catch (IOException e) {
            throw new RuntimeException("Could not create lists of test and application classes.", e);
        }

        MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
        loadClassesIntoMemory(testClasses, classes, memoryLoader);

        // if the report settings are set to false, show empty message
        if (!settings.getShowFullMutationReport()) {
            return Collections.emptyList();
        }

        List<String> testClassNames = testClasses.stream().map(Facade::className).collect(Collectors.toList());
        Class<?> mutationInfrastructureClass;

        try {
            // Create an instance of the loaded class (assuming it has a default constructor)
            mutationInfrastructureClass = loadMutationInfrastructureClass(memoryLoader);

        } catch (Exception e) {
            throw new RuntimeException("Could not load class.", e);
        }

        // create a runner for the tests, currently only JUnit 5 supported
        TestFramework test = new JUnit5();

        // run all tests
        List<String> testResults = test.testing(testClassNames, memoryLoader);

        if (!testResults.isEmpty()) {
            System.out.println("Der erste Test ist fehlgeschlagen" + testResults);
            return Collections.emptyList();
        }

        // get the number of mutation possibilities
        int mutationPossibilities = getMutationPossibilities(mutationInfrastructureClass);

        // List of messages (in Markdown) that will be displayed to students.
        // Leave list empty for no feedback.
        List<String> messages = new ArrayList<>();

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
                mutationInfrastructureClass = loadMutationInfrastructureClass(memoryLoader);

                // Create an instance of MutationInfrastructureClass using the no-argument constructor
                Object mutationInfrastructureInstance = mutationInfrastructureClass.getDeclaredConstructor().newInstance();

                // Set the firstTest field to false
                Field field = mutationInfrastructureClass.getDeclaredField("firstTest");
                field.setAccessible(true);
                field.set(mutationInfrastructureInstance, false);

                // Set the correct variants
                mutationInfrastructureClass.getDeclaredMethod("setCorrectVariants", List.class).invoke(mutationInfrastructureInstance, tmpList);

                // Run test
                testResults = test.testing(testClassNames, memoryLoader);

                // if mutation is not detected, report the defect
                if (testResults.isEmpty()) {
                    messages.add("### Test Mutation Feedback");
                    messages.add("- " + reportDefectiveMutant(mutationInfrastructureClass, i));
                    System.out.println(messages);
                    return messages;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     InstantiationException | ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return messages;
    }

    private void loadClassesIntoMemory(List<Facade> testClasses, List<Facade> classes, MemoryLoader memoryLoader) {
        for (Facade testClass : testClasses) {
            memoryLoader.upload(testClass.className(), testClass.byteCode());
        }
        for (Facade clazz : classes) {
            memoryLoader.upload(clazz.className(), clazz.byteCode());
        }
    }

    @SuppressWarnings("unchecked")
    private int getMutationPossibilities(Class<?> mutationInfrastructureClass) {
        try {
            Field fieldMessage = mutationInfrastructureClass.getDeclaredField("mutationMessageList");
            fieldMessage.setAccessible(true);
            List<String> message = (List<String>) fieldMessage.get(mutationInfrastructureClass.getDeclaredConstructor().newInstance());
            return message.size();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private String reportDefectiveMutant(Class<?> mutationInfrastructureClass, int i) throws NoSuchFieldException, IllegalAccessException {
        Field fieldMessage = mutationInfrastructureClass.getDeclaredField("mutationMessageList");
        fieldMessage.setAccessible(true);
        List<String> message = (List<String>) fieldMessage.get(mutationInfrastructureClass);
        return message.get(i);
    }

    private Class<?> loadMutationInfrastructureClass(MemoryLoader memoryLoader) throws ClassNotFoundException {
        return memoryLoader.loadClass("eu.qped.java.checkers.mutation.MutationInfrastructure");
    }
}