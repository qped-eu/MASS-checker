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

    @Test
    public void classRightClassImplementedTest() {
        String expectedClassTypeName = "Class:TestClass";
        String implementsInterface = "Class:Number";
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
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void classRightInterfaceImplementedTest() {
        String expectedClassTypeName = "Class:TestClass";
        String implementsInterface = "Interface:Number";
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
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void classRightAbstractClassImplementedTest() {
        String expectedClassTypeName = "Class:TestClass";
        String implementsInterface = "AbstractClass:Number";
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
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, designChecker.getDesignFeedbacks().size());
    }

    @Test
    public void classWrongClassTypeTest() {
        String expectedClassTypeName = "Interface:TestClass";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);

        classInfos.add(classInfo);
        qfDesignSettings.setClassInfos(classInfos);

        String source = "class TestClass  {" +
                "}";

        DesignConfigurator designConfigurator = new DesignConfigurator(qfDesignSettings);
        DesignChecker designChecker = new DesignChecker(designConfigurator);
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_CLASS_TYPE, "TestClass");
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }


    @Test
    public void classMissingImplementationTest() {
        String expectedClassTypeName = "Class:TestClass";
        String implementsInterface = "Interface:Number";
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
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.MISSING_INTERFACE_IMPLEMENTATION, "Number");
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }

    @Test
    public void classWrongImplementationTest() {
        String expectedClassTypeName = "Class:TestClass";
        String implementsInterface = "Interface:Number";
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
        designChecker.setSource(source);

        try {
            designChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DesignFeedback fb1 = designFeedbackGenerator.generateFeedback(DesignViolation.WRONG_CLASS_TYPE, "Number");
        List<DesignFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), designChecker.getDesignFeedbacks().toArray());
    }
}
