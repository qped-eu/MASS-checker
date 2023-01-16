package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.exceptions.ClassNameException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.util.*;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class ClassDeclTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private ClassKeywordConfig classConfig;

    private void init() {
        qfClassSettings = new QfClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        classConfig = new ClassKeywordConfig();
    }

    private void setup() {
        classInfo.setClassKeywordConfig(classConfig);
        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    private static void chooseClassType(ClassKeywordConfig classConfig, String classType, KeywordChoice choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("interface", () -> classConfig.setInterfaceType(choice));
        runnableMap.put("class", () -> classConfig.setClassType(choice));
        runnableMap.get(classType).run();
    }

    @DataPoints("classTypes")
    public static String[] classTypeValues() {
        return new String[] {"interface", "class"};
    }

    @DataPoints("choices")
    public static KeywordChoice[] choiceValues() {
        return new KeywordChoice[] {KeywordChoice.YES, KeywordChoice.NO};
    }

    @Theory
    public void noClassNameSpecifiedException() {
        init();
        classConfig.setName("");
        classInfo.setClassKeywordConfig(classConfig);

        String source = "class TestClass {}";

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);


        ClassNameException e = Assertions.assertThrows(ClassNameException.class, () -> classChecker.check(null));
        assertEquals("Class name is required to be able to match classes.", e.getMessage());
    }

    @Theory
    public void matchFullyQualifiedNameTest() {
        init();

        classInfo.setFullyQualifiedName("NotTestClass");
        String source = "class NotTestClass {}";

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

    @Theory
    public void matchProvidedName() {
        init();

        classConfig.setName("NotTestClass");
        String source = "class NotTestClass {}";

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


    @Theory
    public void correctType(@FromDataPoints("classTypes") String classType,
                            @FromDataPoints("choices")KeywordChoice choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.NO)) {
            allowedType = classType.equals("interface") ? "class" : "interface";
        }
        String source = allowedType+" TestClass {}";

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

    @Theory
    public void wrongType(@FromDataPoints("classTypes") String classType,
                            @FromDataPoints("choices")KeywordChoice choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.YES)) {
            allowedType = classType.equals("interface") ? "class" : "interface";
        }
        String source = allowedType+" TestClass {}";

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback(allowedType+" TestClass", "", WRONG_CLASS_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[1];
        expectedFeedback[0] = fb;

        ClassFeedback[] l = classChecker.getClassFeedbacks().toArray(new ClassFeedback[1]);
        l[0] = fb;


        assertArrayEquals(expectedFeedback,l);
    }


    @Theory
    public void correctName() {
        init();
        String source = "class TestClass {}";
        classConfig.setName("TestClass");

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

    @Theory
    public void wrongName() {
        init();

        String source = "class TestClass {}";
        classConfig.setName("NotTestClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "", WRONG_CLASS_NAME);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void wrongTypeAndName(@FromDataPoints("classTypes") String classType,
                                 @FromDataPoints("choices")KeywordChoice choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.NO)) {
            allowedType = classType.equals("interface") ? "class" : "interface";
        }
        String source = allowedType+" TestClass {}";
        classConfig.setName("NotTestClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback(allowedType+" TestClass", "", WRONG_CLASS_NAME);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void notEnoughClasses() {
        init();

        chooseClassType(classConfig, "class", KeywordChoice.YES);

        ClassInfo classInfo1 = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        chooseClassType(classKeywordConfig, "class", KeywordChoice.YES);
        classKeywordConfig.setName("NotTestClass");
        classInfo1.setClassKeywordConfig(classKeywordConfig);
        classInfos.add(classInfo1);

        String source = "class TestClass {}";

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("", "", MISSING_CLASSES);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void multipleClasses() {
        init();

        chooseClassType(classConfig, "class", KeywordChoice.YES);

        FieldKeywordConfig field1 = new FieldKeywordConfig();
        field1.setPrivateModifier(KeywordChoice.YES);
        field1.setType("String");
        field1.setName("name");

        classInfo.setFieldKeywordConfigs(Collections.singletonList(field1));

        ClassInfo classInfo1 = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        chooseClassType(classKeywordConfig, "class", KeywordChoice.YES);
        classKeywordConfig.setName("NotTestClass");

        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field2.setPrivateModifier(KeywordChoice.YES);
        field2.setType("int");
        field2.setName("num");
        classInfo1.setFieldKeywordConfigs(Collections.singletonList(field2));
        classInfo1.setClassKeywordConfig(classKeywordConfig);

        classInfos.add(classInfo1);

        String source = "class TestClass {public String name;}";
        String src1 = "class NotTestClass {public int num;}";

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);
        classChecker.addSource(src1);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "name", WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class NotTestClass", "num", WRONG_ACCESS_MODIFIER);
        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

}
