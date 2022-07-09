package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.qped.java.checkers.classdesign.enums.ClassFeedbackType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class ClassTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private ClassKeywordConfig classConfig;

    private void init() {
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        classConfig = new ClassKeywordConfig();
    }

    private void setup() {
        classInfo.setClassKeywordConfig(classConfig);
        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    private static void chooseClassType(ClassKeywordConfig classConfig, String classType, String choice) {
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
    public static String[] choiceValues() {
        return new String[] {KeywordChoice.YES.toString(), KeywordChoice.NO.toString()};
    }


    @Theory
    public void correctType(@FromDataPoints("classTypes") String classType,
                            @FromDataPoints("choices")String choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.NO.toString())) {
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
                            @FromDataPoints("choices")String choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.YES.toString())) {
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
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
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
                                 @FromDataPoints("choices")String choice) {
        init();

        chooseClassType(classConfig, classType, choice);

        String allowedType = classType;
        if (choice.equals(KeywordChoice.NO.toString())) {
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
}
