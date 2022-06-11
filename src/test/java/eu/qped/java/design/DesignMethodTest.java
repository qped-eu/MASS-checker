package eu.qped.java.design;

import eu.qped.java.checkers.design.*;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;
import eu.qped.java.checkers.design.infos.ClassInfo;
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
    private ClassInfo classInfo;

    @BeforeEach
    public void setup() {
        qfDesignSettings = new QFDesignSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        classInfo.setClassTypeName("class TestClass");
    }

    @Test
    public void optionalNameTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void correctModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");
        methodModifiers.add("private void *");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void multipleWrongNameTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void n_add");
        methodModifiers.add("public void n_subtract");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        System.out.println(designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongTypeTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public int *");
        methodModifiers.add("public int *");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongAccessTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("private void *");
        methodModifiers.add("private void *");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void multipleWrongNonAccessTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public static void add");
        methodModifiers.add("public static void subtract");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingActualMethodTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void add");
        methodModifiers.add("public void subtract");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_METHODS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void noExpectedModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void accessModifierMissingTest() {
        //Generate different feedback?
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("private int add");
        methodModifiers.add("private int subtract");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void expectedMethodsMissingTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public String getNames");
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_METHODS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        System.out.println(designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
