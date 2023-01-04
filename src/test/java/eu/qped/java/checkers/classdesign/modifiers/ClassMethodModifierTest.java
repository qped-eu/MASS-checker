package eu.qped.java.checkers.classdesign.modifiers;

import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.classdesign.TestUtils;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

@RunWith(Theories.class)
public class ClassMethodModifierTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<MethodKeywordConfig> methodKeywordConfigs;
    private MethodKeywordConfig method;

    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public", "private", "protected", ""};
    }

    @DataPoints("reducedAccess")
    public static String[] abstractAccessValues() {
        return new String[] {"public", "protected", ""};
    }


    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
                "static",
                "final",
                "synchronized",
                "native"
        };
    }



    @DataPoint("abstractKeyword")
    public static String abstractKeyword = "abstract";

    @DataPoint("defaultKeyword")
    public static String defaultKeyword = "default";

    @DataPoint("emptyModifier")
    public static String emptyModifier = "";

    @DataPoints("allNonAccessModifierCombinations")
    public static String[][] allNonAccessValues() {
        String[] possibleNonAccess = nonAccessValues();
        return TestUtils.getAllSubsets(Arrays.asList(possibleNonAccess));
    }

    @DataPoints("choices")
    public static String[] choiceValues() {
        return new String[] {KeywordChoice.YES.toString(), KeywordChoice.NO.toString()};
    }

    @DataPoints("exactMatching")
    public static Boolean[] exactValues() {
        return new Boolean[] {true, false};
    }



    //test access modifier
    //test non access modifier
    //test type
    //test name
    //test missing

    private static void chooseAccessModifier(MethodKeywordConfig method, String accessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> method.setPublicModifier(choice));
        runnableMap.put("protected", () -> method.setProtectedModifier(choice));
        runnableMap.put("private", () -> method.setPrivateModifier(choice));
        runnableMap.put("", () -> method.setPackagePrivateModifier(choice));
        runnableMap.get(accessMod).run();
    }


    private static void chooseNonAccessModifier(MethodKeywordConfig method, String nonAccessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("abstract", () -> method.setAbstractModifier(choice));
        runnableMap.put("static", () -> method.setStaticModifier(choice));
        runnableMap.put("final", () -> method.setFinalModifier(choice));
        runnableMap.put("default", () -> method.setDefaultModifier(choice));
        runnableMap.put("synchronized", () -> method.setSynchronizedModifier(choice));
        runnableMap.put("native", () -> method.setNativeModifier(choice));
        runnableMap.put("", () -> method.setEmptyNonAccessModifier(choice));

        runnableMap.get(nonAccessMod).run();
    }

    private void init() {
        qfClassSettings = new QfClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        methodKeywordConfigs = new ArrayList<>();
        method = new MethodKeywordConfig();
        method.setType("int");
        method.setName("test");
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
    }

    private void setup() {
        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    @Theory
    public void constructorTest(@FromDataPoints("accessModifiers") String correctMod) {
        init();

        chooseAccessModifier(method, correctMod, KeywordChoice.YES.toString());
        method.setType("");
        method.setName("TestClass");

        String source = "class TestClass {";
        source += correctMod + " TestClass() {} ";
        source += correctMod + " int test() {}" + "}";

        MethodKeywordConfig method2 = new MethodKeywordConfig();
        chooseAccessModifier(method2, correctMod, KeywordChoice.YES.toString());
        method2.setType("int");
        method2.setName("test");
        methodKeywordConfigs.add(method2);

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
    public void dontCareTest(@FromDataPoints("accessModifiers") String correctMod,
                                @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        init();

        method.setAllowExactModifierMatching(isExactMatch);

        String source = "class TestClass {";
        source += correctMod + " " + String.join(" ", nonAccessComb) + " int test() {}" + "}";

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
    public void correctEmptyNonAccess(@FromDataPoints("accessModifiers") String correctMod,
                               @FromDataPoints("accessModifiers") String wrongMod,
                               @FromDataPoints("emptyModifier") String nonAccessMod,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(nonAccessMod);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        }

        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "class TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
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

    @Theory
    public void faultEmptyNonAccess(@FromDataPoints("accessModifiers") String correctMod,
                                   @FromDataPoints("accessModifiers") String wrongMod,
                                   @FromDataPoints("emptyModifier") String emptyModifier,
                                   @FromDataPoints("choices") String choice,
                                   @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, emptyModifier, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = wrongMod;
        List<String> allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyModifier));
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = correctMod;
            allowedNonAccess = Collections.singletonList(emptyModifier);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "class TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void defaultCorrect(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("defaultKeyword") String nonAccessMod,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(nonAccessMod);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "interface TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
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

    @Theory
    public void defaultAccessFault(@FromDataPoints("reducedAccess") String correctMod,
                               @FromDataPoints("reducedAccess") String wrongMod,
                               @FromDataPoints("defaultKeyword") String nonAccessMod,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = wrongMod;
        List<String> allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = correctMod;
            allowedNonAccess = Collections.singletonList(nonAccessMod);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "interface TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("interface TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }



    @Theory
    public void abstractCorrect(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("abstractKeyword") String abstractKeyword,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(abstractKeyword);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(abstractKeyword));
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "class TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
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
    @Theory
    public void abstractAccessFault(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("abstractKeyword") String abstractKeyword,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();
        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, choice);
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = wrongMod;
        List<String> allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(abstractKeyword));
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = correctMod;
            allowedNonAccess = Collections.singletonList(abstractKeyword);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        String source = "class TestClass {" +
                allowedAccess +
                " "
                + String.join(" ", allowedNonAccess) +
                " int test() {}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }


    @Theory
    public void modifierCorrect(@FromDataPoints("accessModifiers") String correctMod,
                                @FromDataPoints("accessModifiers") String wrongMod,
                                @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(method, nonAccess, choice);
        }

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(nonAccessComb);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int test() {}" + "}";

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
    public void accessFault(@FromDataPoints("accessModifiers") String correctMod,
                                @FromDataPoints("accessModifiers") String wrongMod,
                                @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int test() {}" + "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void nonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                            @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                            @FromDataPoints("choices") String choice,
                            @FromDataPoints("exactMatching") Boolean isExactMatch) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();

        method.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }
        source += correctMod + " " + String.join(" ", allowedNonAccess) + " int test() {}" + "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void accessAndNonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                               @FromDataPoints("accessModifiers") String wrongMod,
                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
        }

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int test() {}" + "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb1};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void multipleAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                            @FromDataPoints("accessModifiers") String wrongMod,
                            @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                            @FromDataPoints("choices") String choice,
                            @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        MethodKeywordConfig method2 = new MethodKeywordConfig();
        method2.setAllowExactModifierMatching(isExactMatch);
        method.setAllowExactModifierMatching(isExactMatch);

        chooseAccessModifier(method, correctMod, choice);
        chooseAccessModifier(method2, correctMod, choice);

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int test() {}";
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int notTest() {}";
        source += "}";

        method2.setType("int");
        method2.setName("notTest");
        methodKeywordConfigs.add(method2);

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "notTest()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);

        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void multipleNonAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                                     @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                                     @FromDataPoints("choices") String choice,
                                     @FromDataPoints("exactMatching") Boolean isExactMatch) {

        List<String> expectedList = Arrays.asList(correctNonAccess);
        init();

        MethodKeywordConfig method2 = new MethodKeywordConfig();
        method2.setAllowExactModifierMatching(isExactMatch);
        method.setAllowExactModifierMatching(isExactMatch);

        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
            chooseNonAccessModifier(method2, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }
        source += correctMod + " " + String.join(" ", allowedNonAccess) + " int test() {}";
        source += correctMod + " " + String.join(" ", allowedNonAccess) + " int notTest() {}";
        source += "}";

        method2.setType("int");
        method2.setName("notTest");
        methodKeywordConfigs.add(method2);

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "notTest()", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);

        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void multipleAccessAndNonAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                                         @FromDataPoints("accessModifiers") String wrongMod,
                                        @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                                        @FromDataPoints("choices") String choice,
                                        @FromDataPoints("exactMatching") Boolean isExactMatch) {

        Assume.assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(correctNonAccess);
        init();

        MethodKeywordConfig method2 = new MethodKeywordConfig();
        method2.setAllowExactModifierMatching(isExactMatch);
        method.setAllowExactModifierMatching(isExactMatch);

        chooseAccessModifier(method, correctMod, choice);
        chooseAccessModifier(method2, correctMod, choice);

        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
            chooseNonAccessModifier(method2, nonAccess, choice);
        }

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int test() {}";
        source += allowedAccess + " " + String.join(" ", allowedNonAccess) + " int notTest() {}";
        source += "}";

        method2.setType("int");
        method2.setName("notTest");
        methodKeywordConfigs.add(method2);

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "test()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "notTest()", ClassFeedbackType.WRONG_ACCESS_MODIFIER);

        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

}
