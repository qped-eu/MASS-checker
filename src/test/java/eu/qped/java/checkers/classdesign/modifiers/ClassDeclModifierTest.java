package eu.qped.java.checkers.classdesign.modifiers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.classdesign.TestUtils;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.KeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

@RunWith(Theories.class)
public class ClassDeclModifierTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private ClassKeywordConfig classConfig;

    @DataPoints("outerClassAccess")
    public static String[] accessValues() {
        return new String[]{"public", ""};
    }

    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
                "abstract",
                "final",
                "",

        };
    }

    @DataPoints("innerClassAccess")
    public static String[] reducedAccess() {
        return new String[] {
                "public", "private", "protected",
                ""
        };
    }

    @DataPoints("innerClassNonAccess")
    public static String[] innerClassNonAccess() {
        return new String[]{
                "abstract",
                "final",
                "",
                "static"

        };
    }

    @DataPoints("choices")
    public static String[] choiceValue() {
        return new String[] {
                KeywordChoice.YES.toString(), KeywordChoice.NO.toString()
        };
    }

    @DataPoints("exactMatching")
    public static Boolean[] exactValues() {
        return new Boolean[] {true, false};
    }


    private static void setAccessModifier(KeywordConfig field, String accessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(choice));
        runnableMap.put("protected", () -> field.setProtectedModifier(choice));
        runnableMap.put("private", () -> field.setPrivateModifier(choice));
        runnableMap.put("", () -> field.setPackagePrivateModifier(choice));
        runnableMap.get(accessMod).run();
    }

    private static void setNonAccessModifier(ClassKeywordConfig classConfig, String nonAccessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("abstract", () -> classConfig.setAbstractModifier(choice));
        runnableMap.put("static", () -> classConfig.setStaticModifier(choice));
        runnableMap.put("final", () -> classConfig.setFinalModifier(choice));
        runnableMap.put("", () -> classConfig.setEmptyNonAccessModifier(choice));

        runnableMap.get(nonAccessMod).run();
    }

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

    @Theory
    public void dontCareTest(@FromDataPoints("outerClassAccess") String correctMod,
                                  @FromDataPoints("nonAccessModifiers") String correctNonAccess) {


        init();
        String source = correctMod +" "+ correctNonAccess+" class TestClass {" +
                "}";

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
    public void innerClassCorrect(@FromDataPoints("innerClassAccess") String correctMod,
                                  @FromDataPoints("innerClassAccess") String wrongMod,
                                  @FromDataPoints("innerClassNonAccess") String correctNonAccess,
                                  @FromDataPoints("choices") String choice,
                                  @FromDataPoints("exactMatching") Boolean isExactMatch) {

        Assume.assumeFalse(correctNonAccess.equals("abstract") && (correctMod.equals("private") || correctMod.equals("protected")));
        Assume.assumeFalse(correctMod.equals(wrongMod));

        init();

        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        innerClassConfig.setAllowExactModifierMatching(isExactMatch);
        setAccessModifier(innerClassConfig, correctMod, choice);
        setNonAccessModifier(innerClassConfig, correctNonAccess, choice);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(innerClassInfo);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(innerClassNonAccess()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }


        String source = " class TestClass {" +
                allowedAccess+" "+String.join(" ", allowedNonAccess)+" class InnerClass {}"+
                "}";

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
    public void innerClassAccessWrong(@FromDataPoints("innerClassAccess") String correctAccess,
                                @FromDataPoints("innerClassAccess") String wrongAccess,
                                @FromDataPoints("innerClassNonAccess") String correctNonAccess,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        Assume.assumeFalse(correctAccess.equals(wrongAccess));

        init();


        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, correctAccess, choice);
        innerClassConfig.setAllowExactModifierMatching(isExactMatch);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(innerClassInfo);

        String allowedAccess = correctAccess;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongAccess;
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }

        String source = "class TestClass {" +
                allowedAccess+" "+String.join(" ", allowedNonAccess)+" class InnerClass {}"+
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class InnerClass", "", ClassFeedbackType.WRONG_CLASS_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void innerClassNonAccessWrong(@FromDataPoints("innerClassAccess") String correctAccess,
                                @FromDataPoints("innerClassNonAccess") String correctNonAccess,
                                @FromDataPoints("innerClassNonAccess") String wrongNonAccess,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        Assume.assumeFalse(correctNonAccess.equals(wrongNonAccess));

        init();


        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setNonAccessModifier(innerClassConfig, correctNonAccess, choice);
        innerClassConfig.setAllowExactModifierMatching(isExactMatch);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(innerClassInfo);

        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(innerClassNonAccess()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }

        String source = "class TestClass {" +
                correctAccess +" "+String.join(" ", allowedNonAccess)+" class InnerClass {}"+
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class InnerClass", "", ClassFeedbackType.WRONG_CLASS_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void innerClassAccessAndNonAccessWrong(@FromDataPoints("innerClassAccess") String correctAccess,
                                            @FromDataPoints("innerClassAccess") String wrongAccess,
                                            @FromDataPoints("innerClassNonAccess") String correctNonAccess,
                                            @FromDataPoints("innerClassNonAccess") String wrongNonAccess,
                                            @FromDataPoints("choices") String choice,
                                            @FromDataPoints("exactMatching") Boolean isExactMatch) {

        Assume.assumeFalse(correctAccess.equals(wrongAccess) || correctNonAccess.equals(wrongNonAccess));

        init();


        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, correctAccess, choice);
        setNonAccessModifier(innerClassConfig, correctNonAccess, choice);
        innerClassConfig.setAllowExactModifierMatching(isExactMatch);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(innerClassInfo);

        String allowedAccess = correctAccess;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);

        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongAccess;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(innerClassNonAccess()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }

        String source = "class TestClass {" +
                allowedAccess+" "+String.join(" ", allowedNonAccess)+" class InnerClass {}"+
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class InnerClass", "", ClassFeedbackType.WRONG_CLASS_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void outerClassCorrect(@FromDataPoints("outerClassAccess") String correctMod,
                                @FromDataPoints("outerClassAccess") String wrongMod,
                                @FromDataPoints("nonAccessModifiers") String correctNonAccess,
                                @FromDataPoints("choices") String choice) {

        Assume.assumeFalse(correctMod.equals(wrongMod));

        init();

        setAccessModifier(classConfig, correctMod, choice);
        setNonAccessModifier(classConfig, correctNonAccess, choice);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }
        String source = allowedAccess +" "+ String.join(" ", allowedNonAccess) +" class TestClass {" +
                "}";

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
    public void outerClassAccessWrong(@FromDataPoints("outerClassAccess") String correctAccess,
                                                  @FromDataPoints("outerClassAccess") String wrongAccess,
                                                  @FromDataPoints("nonAccessModifiers") String correctNonAccess,
                                                  @FromDataPoints("choices") String choice) {

        Assume.assumeFalse(correctAccess.equals(wrongAccess));

        init();
        setNonAccessModifier(classConfig, correctNonAccess, choice);

        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }
        String source = correctAccess +" "+ String.join(" ", allowedNonAccess) +" class TestClass {" +
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.WRONG_CLASS_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));

    }

    @Theory
    public void outerClassNonAccessWrong(@FromDataPoints("outerClassAccess") String correctAccess,
                                      @FromDataPoints("outerClassAccess") String wrongAccess,
                                      @FromDataPoints("nonAccessModifiers") String correctNonAccess,
                                      @FromDataPoints("choices") String choice) {

        Assume.assumeFalse(correctAccess.equals(wrongAccess));

        init();
        setAccessModifier(classConfig, correctAccess, choice);

        String allowedAccess = correctAccess;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongAccess;
        }
        String source = allowedAccess +" "+ String.join(" ", allowedNonAccess) +" class TestClass {" +
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.WRONG_CLASS_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));

    }

    @Theory
    public void outerClassAccessAndNonAccessWrong(@FromDataPoints("outerClassAccess") String correctAccess,
                                @FromDataPoints("outerClassAccess") String wrongAccess,
                                @FromDataPoints("nonAccessModifiers") String correctNonAccess,
                                @FromDataPoints("choices") String choice) {

        Assume.assumeFalse(correctAccess.equals(wrongAccess));

        init();
        setAccessModifier(classConfig, correctAccess, choice);
        setNonAccessModifier(classConfig, correctNonAccess, choice);

        String allowedAccess = correctAccess;
        List<String> allowedNonAccess = Collections.singletonList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongAccess;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(correctNonAccess));
            Assume.assumeFalse(allowedNonAccess.contains("abstract") && allowedNonAccess.contains("final"));
        }
        String source = allowedAccess +" "+ String.join(" ", allowedNonAccess) +" class TestClass {" +
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.WRONG_CLASS_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));

    }

}
