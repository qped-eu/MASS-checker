package eu.qped.java.checkers.classdesign;


import eu.qped.java.checkers.classdesign.enums.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private FieldKeywordConfig field;


    @BeforeEach
    private void init() {
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        fieldKeywordConfigs = new ArrayList<>();
        field = new FieldKeywordConfig();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
    }

    private void setup() {

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    @Test
    public void correctNameAndType() {
        String source = "class TestClass { int a;"+ "}";
        field.setType("int");
        field.setName("a");

        setup();
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

    @Test
    public void wrongType() {
        String source = "class TestClass { double a;"+ "}";
        field.setType("int");
        field.setName("a");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ELEMENT_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }


    @Test
    public void wrongName() {
        String source = "class TestClass { int b;"+ "}";
        field.setType("int");
        field.setName("a");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_ELEMENT_NAME);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Test
    public void wrongTypeAndName() {
        String source = "class TestClass { double b;"+ "}";
        field.setType("int");
        field.setName("a");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_ELEMENT_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Test
    public void expectedMoreFields() {
        String source = "class TestClass { double b;"+ "}";
        field.setType("int");
        field.setName("a");

        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field.setType("double");
        field.setName("b");
        fieldKeywordConfigs.add(field2);

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.MISSING_FIELDS);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

}
