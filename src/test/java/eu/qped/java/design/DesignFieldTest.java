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

public class DesignFieldTest {

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
    public void multipleVariablesTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int a");
        fieldModifiers.add("public int b");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public int a = 0, b = 0;" +
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
    public void missingAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("List<String> *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "List<String> names = new ArrayList<String>();" +
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
    public void optionalTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("* * String *");
        fieldModifiers.add("private String name");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "private String year;" +
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
    public void rightModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String name");
        fieldModifiers.add("public int year");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "public int year;" +
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
    public void wrongNameTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String not_name");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        System.out.println(fb1.toString());
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongTypeTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "private String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        //System.out.println("Feedback: "+designChecker.getDesignFeedbacks().get(0).toString());

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongNonAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("public final int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "public static int year;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "year", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        System.out.println("Feedback: "+ designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingFieldTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int *");
        fieldModifiers.add("public String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_FIELDS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void expectedFieldMissingTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private static double name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_FIELDS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        System.out.println(designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongNameTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String not_name1");
        fieldModifiers.add("public String not_name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongTypeTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int name1");
        fieldModifiers.add("public int name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("public String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name1;" +
                "private String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void multipleWrongNonAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public static final String *");
        fieldModifiers.add("public static final String *");
        fieldModifiers.add("public int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public static String name1;" +
                "public String name2;" +
                "public static int name3;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb3 = DesignFeedbackGenerator.generateFeedback("TestClass", "name3", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        expectedFeedback.add(fb3);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void ambiguityTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("final String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public final String name1;" + //access issue, both have one error
                "public final String name2;" + //non access issue
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void noModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
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


}
