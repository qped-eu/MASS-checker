package eu.qped.java.design;

import eu.qped.java.checkers.design.*;
import eu.qped.java.checkers.mass.QFDesignSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesignMethodTest {

    private QFDesignSettings qfDesignSettings;
    private ArrayList<ClassInfo> classInfos;
    private final DesignFeedbackGenerator designFeedbackGenerator = DesignFeedbackGenerator.createDesignFeedbackGenerator();

    @BeforeEach
    public void setup() {
        qfDesignSettings = new QFDesignSettings();
        classInfos = new ArrayList<>();
        qfDesignSettings.setModifierMaxRestrictive(false);
    }

    @Test
    public void maxRestrictedTest() {
        ClassInfo classInfo = new ClassInfo();

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);
        qfDesignSettings.setModifierMaxRestrictive(true);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.METHODS_NOT_RESTRICTIVE_ENOUGH, "");
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void emptyModifiersTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void missingMethodTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public");
        methodModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.MISSING_METHODS, "");

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void moreExpectedThanActualNonAccessTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public static");
        methodModifiers.add("public static");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";


        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_NON_ACCESS_MODIFIER, "add");
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_NON_ACCESS_MODIFIER, "subtract");


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void noModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "private void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void expectedModifierMissingTest() {
        //Generate different feedback?
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "void add() {" +
                "int addnum = 0;" +
                "}" +
                "void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_ACCESS_MODIFIER, "add");
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_ACCESS_MODIFIER, "subtract");

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void correctModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public");
        methodModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "private void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void wrongAccessModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public");
        methodModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_ACCESS_MODIFIER, "subtract");

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void moreActualThanExpectedTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public");
        methodModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public static void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_NON_ACCESS_MODIFIER, "subtract");

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
