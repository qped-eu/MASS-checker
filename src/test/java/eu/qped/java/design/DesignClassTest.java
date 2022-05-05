package eu.qped.java.design;

import eu.qped.java.checkers.design.*;
import eu.qped.java.checkers.mass.QFDesignSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesignClassTest {

    private QFDesignSettings qfDesignSettings;
    private ArrayList<ClassInfo> classInfos;
    private final DesignFeedbackGenerator designFeedbackGenerator = DesignFeedbackGenerator.createDesignFeedbackGenerator();

    @BeforeEach
    public void setup() {
        qfDesignSettings = new QFDesignSettings();
        classInfos = new ArrayList<>();
    }

    /**
     * Example Test of what a more involved solution would look like and an example of provided feedback
     */
    @Test
    public void multipleClassTest() {
        ClassInfo interfaceInfo = new ClassInfo();
        interfaceInfo.setClassTypeName("interface:Number");
        ArrayList<String> methodKeywords = new ArrayList<>();
        methodKeywords.add("private");
        methodKeywords.add("private");
        methodKeywords.add("default");
        methodKeywords.add("default");
        interfaceInfo.setMethodKeywords(methodKeywords);


        //Expected Class Infos
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:HexaDecimal");

        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("interface:Number");
        classInfo.setInheritsFrom(inheritsFrom);

        ArrayList<String> fieldKeywords = new ArrayList<>();
        fieldKeywords.add("private");
        classInfo.setFieldKeywords(fieldKeywords);
        ArrayList<String> classMethodKeywords = new ArrayList<>();
        classMethodKeywords.add("private");
        classMethodKeywords.add("private");
        classInfo.setMethodKeywords(classMethodKeywords);

        classInfos.add(interfaceInfo);
        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String interfaceSource = "public abstract class Number2 {" +
                "private int toIntValue();"+
                "private void fromIntValue(int value){" +
                "}"+
                "void add(Number number){" +
                "}"+
                "void subtract(Number number){" +
                "}"+
                "}";

        String classSource = "public class HexaDecimal extends Number2 {" +
                "private String value;"+
                "private int toIntValue() {" +
                "}"+
                "private int fromIntValue() {" +
                "}"+
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(interfaceSource);
        designChecker.addSourceCode(classSource);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_CLASS_TYPE, "Number");
//        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_INHERITED_CLASS_TYPE, "Number");
        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("Number", "", DesignViolation.WRONG_CLASS_TYPE);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("Number2", "", DesignViolation.WRONG_CLASS_NAME);
        //DesignFeedback fb2 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_INHERITED_CLASS_TYPE, "Number");


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void classRightClassImplementedTest() {
        String expectedClassTypeName = "class:TestClass";
        String implementsInterface = "class:Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void classRightInterfaceImplementedTest() {
        String expectedClassTypeName = "class:TestClass";
        String implementsInterface = "interface:Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number {" +
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
    public void classRightAbstractClassImplementedTest() {
        String expectedClassTypeName = "class:TestClass";
        String implementsInterface = "abstract class:Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void classWrongClassTypeTest() {
        String expectedClassTypeName = "interface:TestClass";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass  {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignViolation.WRONG_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void classMissingImplementationTest() {
        String expectedClassTypeName = "class:TestClass";
        String implementsInterface = "interface:Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass{" +
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
                "Number", DesignViolation.MISSING_INTERFACE_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void classWrongImplementationTest() {
        String expectedClassTypeName = "class:TestClass";
        String implementsInterface = "interface:Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number{" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addSourceCode(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "Number", DesignViolation.WRONG_INHERITED_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
