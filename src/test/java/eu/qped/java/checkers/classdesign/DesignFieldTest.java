package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class DesignFieldTest {


    @Test
    public void multipleVariablesTest() {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field1Config = new FieldKeywordConfig();
        field1Config.setPublicModifier(KeywordChoice.YES.toString());
        field1Config.setFinalModifier(KeywordChoice.YES.toString());
        field1Config.setStaticModifier(KeywordChoice.YES.toString());
        field1Config.setType("int");
        field1Config.setName("a");
        FieldKeywordConfig field2Config = new FieldKeywordConfig();
        field2Config.setPublicModifier(KeywordChoice.YES.toString());
        field2Config.setType("int");
        field2Config.setName("b");

        fieldKeywordConfigs.add(field1Config);
        fieldKeywordConfigs.add(field2Config);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public final static int a = 0, b = 0;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, classChecker.getClassFeedbacks().size());
    }
//
//
//
//    @Test
//    public void wrongNameTest() {
//        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
//        FieldKeywordConfig field1Config = new FieldKeywordConfig();
//        field1Config.setPublicModifier(KeywordChoice.YES.toString());
//        field1Config.setFieldType("String");
//        field1Config.setFieldName("not_name");
//        fieldKeywordConfigs.add(field1Config);
//        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public String name;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "name",
//                ClassFeedbackType.WRONG_ELEMENT_NAME);
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }

//    @Test
//    public void wrongTypeTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        fieldModifiers.add("private int *");
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "private String name;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "name",
//                ClassFeedbackType.WRONG_ELEMENT_TYPE);
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void wrongAccessModifierTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        fieldModifiers.add("private String *");
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public String name;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "name",
//                ClassFeedbackType.ACCESS_NOT_RESTRICTIVE_ENOUGH);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void wrongNonAccessModifierTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        fieldModifiers.add("public String *");
//        fieldModifiers.add("public final int *");
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public String name;" +
//                "public static int year;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "year",
//                ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void missingFieldTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        fieldModifiers.add("public int *");
//        fieldModifiers.add("public String *");
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "public String name1;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "",
//                ClassFeedbackType.MISSING_FIELDS);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void expectedFieldMissingTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        fieldModifiers.add("private static double name2");
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "String name;" +
//                "int year;" +
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
//        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("class TestClass", "",
//                ClassFeedbackType.MISSING_FIELDS);
//
//        List<ClassFeedback> expectedFeedback = new ArrayList<>();
//        expectedFeedback.add(fb1);
//        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
//    }
//
//    @Test
//    public void noModifierTest() {
//        ArrayList<String> fieldModifiers = new ArrayList<>();
//        classInfo.setFieldKeywordConfigs(fieldModifiers);
//
//        classInfos.add(classInfo);
//        qfClassSettings.setClassInfos(classInfos);
//
//        String source = "class TestClass {" +
//                "String name;" +
//                "int year;" +
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


}
