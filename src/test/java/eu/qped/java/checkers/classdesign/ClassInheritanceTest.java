package eu.qped.java.checkers.classdesign;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.InheritsFromConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

@RunWith(Theories.class)
public class ClassInheritanceTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private InheritsFromConfig inheritsConfig;
    private List<InheritsFromConfig> inheritsFromConfigList;

    private void init() {
        qfClassSettings = new QfClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        inheritsConfig = new InheritsFromConfig();
        inheritsFromConfigList = new ArrayList<>();
    }

    private void setup() {
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        inheritsFromConfigList.add(inheritsConfig);
        classInfo.setInheritsFromConfigs(inheritsFromConfigList);
        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    private static void chooseInheritsType(InheritsFromConfig inheritsConfig, String classType, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("interface", () -> inheritsConfig.setInterfaceType(choice));
        runnableMap.put("class", () -> inheritsConfig.setClassType(choice));
        runnableMap.get(classType).run();
    }

    @DataPoints("classTypes")
    public static String[] classTypeValues() {
        return new String[] {"interface", "class"};
    }

    @DataPoints("inheritsKeyword")
    public static String[] inheritsKeywordValues() {
        return new String[] {"implements", "extends"};
    }

    @DataPoints("choices")
    public static String[] choiceValues() {
        return new String[] {KeywordChoice.YES.toString(), KeywordChoice.NO.toString()};
    }

    @Theory
    public void correctType(@FromDataPoints("classTypes") String inheritsType,
                            @FromDataPoints("inheritsKeyword") String inheritsKeyword,
                            @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));

        chooseInheritsType(inheritsConfig, inheritsType, choice);

        String allowedKeyword = inheritsKeyword;
        if (choice.equals(KeywordChoice.NO.toString())) {
            allowedKeyword = inheritsType.equals("interface") ? "extends" : "implements";
        }
        String source = "class TestClass "+allowedKeyword+" SuperClass{}";

        inheritsConfig.setName("SuperClass");

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
    public void wrongType(@FromDataPoints("classTypes") String inheritsType,
                            @FromDataPoints("inheritsKeyword") String inheritsKeyword,
                            @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));

        chooseInheritsType(inheritsConfig, inheritsType, choice);

        String allowedKeyword = inheritsKeyword;
        if (choice.equals(KeywordChoice.YES.toString())) {
            allowedKeyword = inheritsType.equals("interface") ? "extends" : "implements";
        }
        String source = "class TestClass "+allowedKeyword+" SuperClass{}";

        inheritsConfig.setName("SuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.WRONG_SUPER_CLASS_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }


    @Theory
    public void correctName(@FromDataPoints("classTypes") String inheritsType,
                            @FromDataPoints("inheritsKeyword") String inheritsKeyword) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));

        String source = "class TestClass "+inheritsKeyword+" SuperClass{}";

        inheritsConfig.setName("SuperClass");

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
    public void wrongName(@FromDataPoints("classTypes") String inheritsType,
                            @FromDataPoints("inheritsKeyword") String inheritsKeyword) {
        init();
        Map<String, ClassFeedbackType> nameViolations = new HashMap<>();
        nameViolations.put("class", ClassFeedbackType.DIFFERENT_CLASS_NAMES_EXPECTED);
        nameViolations.put("interface", ClassFeedbackType.DIFFERENT_INTERFACE_NAMES_EXPECTED);

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));

        String source = "class TestClass "+inheritsKeyword+" SuperClass{}";
        chooseInheritsType(inheritsConfig, inheritsType, KeywordChoice.YES.toString());

        inheritsConfig.setName("NotSuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", nameViolations.get(inheritsType));
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void wrongInterfaceAndName() {
        init();

        chooseInheritsType(inheritsConfig, "interface", KeywordChoice.YES.toString());
        String source = "class TestClass extends SuperClass{}";
        inheritsConfig.setName("NotSuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.MISSING_INTERFACE_IMPLEMENTATION);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void wrongClassAndName() {
        init();

        chooseInheritsType(inheritsConfig, "class", KeywordChoice.YES.toString());
        String source = "class TestClass implements SuperClass{}";
        inheritsConfig.setName("NotSuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.MISSING_CLASS_EXTENSION);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void missingWithTwoChoices() {
        init();

        String source = "class TestClass implements SuperClass{}";
        inheritsConfig.setName("NotSuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.MISSING_SUPER_CLASS);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void dontCareTest(@FromDataPoints("classTypes") String inheritsType,
                             @FromDataPoints("inheritsKeyword") String inheritsKeyword) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));

        String source = "class TestClass "+ inheritsKeyword +" SuperClass{}";
        inheritsConfig.setName("SuperClass");

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
    public void missingInterface(@FromDataPoints("classTypes") String inheritsType,
                            @FromDataPoints("inheritsKeyword") String inheritsKeyword,
                            @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));
        Assume.assumeFalse(inheritsType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));
        Assume.assumeFalse(inheritsType.equals("class") && choice.equals(KeywordChoice.YES.toString()));

        chooseInheritsType(inheritsConfig, inheritsType, choice);
        String source = "class TestClass {}";
        inheritsConfig.setName("SuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.MISSING_INTERFACE_IMPLEMENTATION);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void missingClass(@FromDataPoints("classTypes") String inheritsType,
                                 @FromDataPoints("inheritsKeyword") String inheritsKeyword,
                                 @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsType.equals("class") && inheritsKeyword.equals("implements"));
        Assume.assumeFalse(inheritsType.equals("interface") && inheritsKeyword.equals("extends"));
        Assume.assumeFalse(inheritsType.equals("interface") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsType.equals("class") && choice.equals(KeywordChoice.NO.toString()));

        chooseInheritsType(inheritsConfig, inheritsType, choice);
        String source = "class TestClass {}";
        inheritsConfig.setName("SuperClass");

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SuperClass", ClassFeedbackType.MISSING_CLASS_EXTENSION);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void correctMultipleInterfaces(@FromDataPoints("classTypes") String inheritsFirstType,
                                            @FromDataPoints("choices")String choice) {
        init();


        Assume.assumeFalse(inheritsFirstType.equals("class") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsFirstType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));

        InheritsFromConfig inheritsConfig2 = new InheritsFromConfig();
        chooseInheritsType(inheritsConfig2, inheritsFirstType, choice);
        chooseInheritsType(inheritsConfig, inheritsFirstType, choice);

        String source = "class TestClass implements SuperClass, SecondSuperClass{}";

        inheritsConfig2.setName("SecondSuperClass");
        inheritsConfig.setName("SuperClass");

        inheritsFromConfigList.add(inheritsConfig2);

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
    public void correctOneClassOneInterface(@FromDataPoints("classTypes") String inheritsFirstType,
                                          @FromDataPoints("classTypes") String inheritsSecondType,
                                          @FromDataPoints("choices")String choice) {
        init();


        Assume.assumeFalse(inheritsFirstType.equals("class") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsFirstType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));
        Assume.assumeFalse(inheritsSecondType.equals("interface") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsSecondType.equals("class") && choice.equals(KeywordChoice.NO.toString()));

        InheritsFromConfig inheritsConfig2 = new InheritsFromConfig();
        chooseInheritsType(inheritsConfig2, inheritsSecondType, choice);
        chooseInheritsType(inheritsConfig, inheritsFirstType, choice);

        String source = "class TestClass extends SecondSuperClass implements SuperClass {}";

        inheritsConfig2.setName("SecondSuperClass");
        inheritsConfig.setName("SuperClass");

        inheritsFromConfigList.add(inheritsConfig2);

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
    public void wrongOneInterfaceOneClass(@FromDataPoints("classTypes") String inheritsFirstType,
                                            @FromDataPoints("choices")String choice) {
        init();


        Assume.assumeFalse(inheritsFirstType.equals("class") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsFirstType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));

        InheritsFromConfig inheritsConfig2 = new InheritsFromConfig();
        chooseInheritsType(inheritsConfig2, inheritsFirstType, choice);
        chooseInheritsType(inheritsConfig, inheritsFirstType, choice);

        String source = "class TestClass extends SecondSuperClass implements SuperClass {}";

        inheritsConfig2.setName("SecondSuperClass");
        inheritsConfig.setName("SuperClass");

        inheritsFromConfigList.add(inheritsConfig2);

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SecondSuperClass", ClassFeedbackType.WRONG_SUPER_CLASS_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void wrongTwoInterfaces(@FromDataPoints("classTypes") String inheritsFirstType,
                                          @FromDataPoints("classTypes") String inheritsSecondType,
                                          @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsFirstType.equals("class") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsFirstType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));
        Assume.assumeFalse(inheritsSecondType.equals("interface") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsSecondType.equals("class") && choice.equals(KeywordChoice.NO.toString()));

        InheritsFromConfig inheritsConfig2 = new InheritsFromConfig();
        chooseInheritsType(inheritsConfig2, inheritsSecondType, choice);
        chooseInheritsType(inheritsConfig, inheritsFirstType, choice);

        String source = "class TestClass implements SuperClass, SecondSuperClass {}";

        inheritsConfig2.setName("SecondSuperClass");
        inheritsConfig.setName("SuperClass");

        inheritsFromConfigList.add(inheritsConfig2);

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "SecondSuperClass", ClassFeedbackType.WRONG_SUPER_CLASS_TYPE);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void wrongMultipleInterfaceNames(@FromDataPoints("classTypes") String inheritsFirstType,
                                          @FromDataPoints("choices")String choice) {
        init();

        Assume.assumeFalse(inheritsFirstType.equals("class") && choice.equals(KeywordChoice.YES.toString()));
        Assume.assumeFalse(inheritsFirstType.equals("interface") && choice.equals(KeywordChoice.NO.toString()));

        InheritsFromConfig inheritsConfig2 = new InheritsFromConfig();
        chooseInheritsType(inheritsConfig2, inheritsFirstType, choice);
        chooseInheritsType(inheritsConfig, inheritsFirstType, choice);

        String source = "class TestClass implements NotSuperClass, NotSecondSuperClass{}";

        inheritsConfig2.setName("SecondSuperClass");
        inheritsConfig.setName("SuperClass");

        inheritsFromConfigList.add(inheritsConfig2);

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "NotSuperClass", ClassFeedbackType.DIFFERENT_INTERFACE_NAMES_EXPECTED);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "NotSecondSuperClass", ClassFeedbackType.DIFFERENT_INTERFACE_NAMES_EXPECTED);
        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }
}

