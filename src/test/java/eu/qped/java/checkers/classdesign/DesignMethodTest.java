package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesignMethodTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
//    @BeforeEach
//    public void setup() {
//        qfClassSettings = new QFClassSettings();
//        classInfos = new ArrayList<>();
//        classInfo = new ClassInfo();
//        classInfo.setClassKeywords("class TestClass");
//    }
//
//    @Test
//    public void correctModifierTest() {
//        ArrayList<String> methodModifiers = new ArrayList<>();
//        methodModifiers.add("public void *");
//        methodModifiers.add("private void *");
//        classInfo.setMethodKeywordConfigs(methodModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public void add() {" +
//                "int addnum = 0;" +
//                "}" +
//                "private void subtract() {" +
//                "int subnum = 0;" +
//                "}" +
//                "}";
//
//        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
//        ClassChecker classChecker = new ClassChecker(classConfigurator);
//        classChecker.addSource(source);
//
//        try {
//            classChecker.check(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        assertEquals(0, classChecker.getClassFeedbacks().size());
//    }
//
//
//
//
//    @Test
//    public void missingMethodTest() {
//        ArrayList<String> methodModifiers = new ArrayList<>();
//        methodModifiers.add("public void add");
//        methodModifiers.add("public void subtract");
//        classInfo.setMethodKeywordConfigs(methodModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public void add() {" +
//                "int addnum = 0;" +
//                "}" +
//                "}";
//
//        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
//        ClassChecker classChecker = new ClassChecker(classConfigurator);
//        classChecker.addSource(source);
//
//        try {
//            classChecker.check(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "", ClassFeedbackType.MISSING_METHODS);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void noExpectedModifierTest() {
//        ArrayList<String> methodModifiers = new ArrayList<>();
//        classInfo.setMethodKeywordConfigs(methodModifiers);
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public void add() {" +
//                "int addnum = 0;" +
//                "}" +
//                "private void subtract() {" +
//                "int subnum = 0;" +
//                "}" +
//                "}";
//
//        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
//        ClassChecker classChecker = new ClassChecker(classConfigurator);
//        classChecker.addSource(source);
//
//        try {
//            classChecker.check(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        assertEquals(0, classChecker.getClassFeedbacks().size());
//    }
//
//    @Test
//    public void accessModifierMissingTest() {
//        //Generate different feedback?
//        ArrayList<String> methodModifiers = new ArrayList<>();
//        methodModifiers.add("private int add");
//        methodModifiers.add("private int subtract");
//        classInfo.setMethodKeywordConfigs(methodModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "void add() {" +
//                "int addnum = 0;" +
//                "}" +
//                "void subtract() {" +
//                "int subnum = 0;" +
//                "}" +
//                "}";
//
//        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
//        ClassChecker classChecker = new ClassChecker(classConfigurator);
//        classChecker.addSource(source);
//
//        try {
//            classChecker.check(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "add()", ClassFeedbackType.ACCESS_NOT_RESTRICTIVE_ENOUGH);
//        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("class TestClass", "subtract()", ClassFeedbackType.ACCESS_NOT_RESTRICTIVE_ENOUGH);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//        expectedFeedback.add(fb2);
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void expectedMethodsMissingTest() {
//        ArrayList<String> methodModifiers = new ArrayList<>();
//        methodModifiers.add("public String getNames");
//        classInfo.setMethodKeywordConfigs(methodModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "void add() {" +
//                "int addnum = 0;" +
//                "}" +
//                "void subtract() {" +
//                "int subnum = 0;" +
//                "}" +
//                "}";
//
//        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
//        ClassChecker classChecker = new ClassChecker(classConfigurator);
//        classChecker.addSource(source);
//
//        try {
//            classChecker.check(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "", ClassFeedbackType.MISSING_METHODS);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
}
