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

public class DesignClassTest {

    private QFDesignSettings qfDesignSettings;
    private ArrayList<ClassInfo> classInfos;

    @BeforeEach
    public void setup() {
        qfDesignSettings = new QFDesignSettings();
        classInfos = new ArrayList<>();
    }

    @Test
    public void optionalAccessTest() {
        String expectedClass1 = "* class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
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
    public void optionalNonAccessTest() {
        String expectedClass1 = "public * class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "public class TestClass {" +
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
    public void optionalTypeTest() {
        String expectedClass1 = "* TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "interface TestClass {" +
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
    public void wrongAccessTest() {
        String expectedClass1 = "public class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongNonAccessTest() {
        String expectedClass1 = "abstract class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_NON_ACCESS_MODIFIER);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongTypeTest() {
        String expectedClass1 = "interface TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_TYPE);

        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    /**
     * As the checker sees that after matching up corresponding ones,
     * only one expected class and one class declaration exist, both of them are matched up together, even if the name is wrong.
     * This does not happen if there is more than one class decl or info as the checker does not generate further feedback if the names can not be matched up.
     */
    @Test
    public void matchFoundWithOneClassTest() {
        String expectedClass1 = "class TestClass1";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("interface Number");
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);
        classInfo1.setInheritsFrom(inheritsFrom);

        String expectedClass2 = "class TestClass2";
        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.setClassTypeName(expectedClass2);

        classInfos.add(classInfo1);
        classInfos.add(classInfo2);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}" +
                "class TestClass2 {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_NAME);
        DesignFeedback fb2 = DesignFeedbackGenerator.generateFeedback("TestClass", "Number", DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void innerClassTest() {
        String expectedOuterClass = "class OuterClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedOuterClass);

        String expectedInnerClass = "class InnerClass";
        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.setClassTypeName(expectedInnerClass);

        classInfos.add(classInfo1);
        classInfos.add(classInfo2);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class OuterClass {" +
                "class InnerClass {" +
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
    public void multipleInheritanceTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType1 = "interface Number";
        String implementsType2 = "abstract class Number1";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType1);
        inheritsFrom.add(implementsType2);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number1 implements Number {" +
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
    public void rightClassImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void rightInterfaceImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "interface Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number {" +
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
    public void rightAbstractClassImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "abstract class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void wrongInterfaceTypeTest() {
        String expectedClassTypeName = "class TestClass";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "interface TestClass  {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "", DesignFeedbackGenerator.WRONG_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void missingImplementationTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass",
                "", DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "class Number";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass",
                "", DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingAbstractClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass",
                "", DesignFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void missingFinalClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "final class Number";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass",
                "", DesignFeedbackGenerator.MISSING_FINAL_CLASS_IMPLEMENTATION);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongImplementationTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "Number",
                DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "Number",
                DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongAbstractClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "Number",
                DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongInheritedClassNameTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number2";
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
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "",
                DesignFeedbackGenerator.DIFFERENT_CLASS_NAMES_EXPECTED);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void wrongInheritedInterfaceNameTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number2";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.addCompilationUnit(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = DesignFeedbackGenerator.generateFeedback("TestClass", "",
                DesignFeedbackGenerator.DIFFERENT_INTERFACE_NAMES_EXPECTED);
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
