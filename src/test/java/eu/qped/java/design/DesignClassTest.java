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

    //TODO
    //Multiple Inheritance

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
        methodKeywords.add("private int toIntValue");
        methodKeywords.add("private void fromIntValue");
        methodKeywords.add("default void add");
        methodKeywords.add("default void subtract");
        interfaceInfo.setMethodKeywords(methodKeywords);


        //Expected Class Infos
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName("class:HexaDecimal");

        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("interface:Number");
        classInfo.setInheritsFrom(inheritsFrom);

        ArrayList<String> fieldKeywords = new ArrayList<>();
        fieldKeywords.add("private String *");
        classInfo.setFieldKeywords(fieldKeywords);
        ArrayList<String> classMethodKeywords = new ArrayList<>();
        classMethodKeywords.add("private int *");
        classMethodKeywords.add("private int *");
        classInfo.setMethodKeywords(classMethodKeywords);

        classInfos.add(interfaceInfo);
        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String interfaceSource = "public interface Number2 {" +
                "private int toIntValue();"+
                "private void fromIntValue(int value){" +
                "}"+
                "void add(Number number){" +
                "}"+
                "void subtract(Number number){" +
                "}"+
                "}";

        String classSource = "public class HexaDecimal implements Number2 {" +
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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("Number2", "", DesignFeedbackGenerator.WRONG_CLASS_NAME);
        DesignFeedback fb2 = designFeedbackGenerator.generateFeedback("HexaDecimal", "Number2", DesignFeedbackGenerator.WRONG_INHERITED_CLASS_NAME);


        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void rightClassImplementedTest() {
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
    public void rightInterfaceImplementedTest() {
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
    public void rightAbstractClassImplementedTest() {
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
    public void wrongClassTypeTest() {
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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void missingImplementationTest() {
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
                "", DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongImplementationTest() {
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

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback("TestClass", "Number", DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
