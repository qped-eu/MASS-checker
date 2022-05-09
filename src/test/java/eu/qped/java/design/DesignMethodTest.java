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
    public void optionalNameTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void maxRestrictedTest() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");

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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass",
                "", DesignFeedbackGenerator.METHODS_NOT_RESTRICTIVE_ENOUGH);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void correctModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");
        methodModifiers.add("private void *");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongTypeTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public int *");
        methodModifiers.add("public int *");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);


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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);


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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_METHODS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void noExpectedModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void expectedModifiersMissingTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "add()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "subtract()", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
