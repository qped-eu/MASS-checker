package eu.qped.java.design;

import eu.qped.java.checkers.design.*;
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
        classInfo.setClassTypeName("class:TestClass");

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);
        qfDesignSettings.setModifierMaxRestrictive(true);

        String source = "class TestClass {" +
                "private String name;" +
                "public int year;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignViolation.FIELDS_NOT_RESTRICTIVE_ENOUGH);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void emptyModifiersTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "public int year;" +
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
    public void rightModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public");
        fieldModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "public int year;" +
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
    public void wrongAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignViolation.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        System.out.println("Feedback: "+designChecker.getDesignFeedbacks().get(0).toString());

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongNonAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public");
        fieldModifiers.add("public final");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "public static int year;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "year", DesignViolation.WRONG_NON_ACCESS_MODIFIER);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        System.out.println("Feedback: "+designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingFieldTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public");
        fieldModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignViolation.MISSING_FIELDS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public");
        fieldModifiers.add("public");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name1;" +
                "private String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignViolation.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignViolation.WRONG_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void multipleWrongNonAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public static final");
        fieldModifiers.add("public static final");
        fieldModifiers.add("public static final");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public static String name1;" +
                "public static String name2;" +
                "public static String name3;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignViolation.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignViolation.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb3 = designFeedbackGenerator.generateFeedback("TestClass", "name3", DesignViolation.WRONG_NON_ACCESS_MODIFIER);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        expectedFeedback.add(fb3);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void noModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
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
    public void expectedModifierMissingTest() {
        //Generate different feedback?
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignViolation.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "year", DesignViolation.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
