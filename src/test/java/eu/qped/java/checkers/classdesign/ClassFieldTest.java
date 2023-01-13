package eu.qped.java.checkers.classdesign;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

public class ClassFieldTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private FieldKeywordConfig field;

    @BeforeEach
    void init() {
        qfClassSettings = new QfClassSettings();
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
    public void optionalName() {
        String source = "class TestClass { int a;"+ "}";
        field.setType("int");
        field.setName("");

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
    public void optionalType() {
        String source = "class TestClass { int a;"+ "}";
        field.setType("");
        field.setName("?");

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
        field.setType("double");
        field.setName("b");

        FieldKeywordConfig field2 = new FieldKeywordConfig();

        field2.setType("int");
        field2.setName("a");
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

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.MISSING_FIELDS);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Test
    public void expectedFewerFields() {
        String source = "class TestClass { double a, b;"+ "}";
        field.setType("double");
        field.setName("b");

        classInfo.setMatchExactFieldAmount(true);

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.TOO_MANY_FIELDS);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Test
    public void unrollVariablesTest() {
        String source = "class TestClass { double a = 1, b = 2;"+ "}";
        field.setType("double");
        field.setName("a");

        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field2.setType("double");
        field2.setName("b");
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

        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

}
