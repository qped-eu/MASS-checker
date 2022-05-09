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
    public void optionalTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String *");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.FIELDS_NOT_RESTRICTIVE_ENOUGH);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void rightModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String name");
        fieldModifiers.add("public int year");

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
    public void wrongNameTest() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");

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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongTypeTest() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");

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
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String *");

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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "year", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        System.out.println("Feedback: "+designChecker.getDesignFeedbacks().get(0).toString());
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingFieldTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int *");
        fieldModifiers.add("public String *");

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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.MISSING_FIELDS);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void expectedModifierMissingTest() {
        //Generate different feedback?
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private double *");

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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "year", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void multipleWrongNameTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String not_name1");
        fieldModifiers.add("public String not_name2");

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ELEMENT_NAME);


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

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:TestClass");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);


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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);


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
        fieldModifiers.add("public static final String *");

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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "name1", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("TestClass", "name2", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        DesignFeedback fb3 = designFeedbackGenerator.generateFeedback("TestClass", "name3", DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


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


}
