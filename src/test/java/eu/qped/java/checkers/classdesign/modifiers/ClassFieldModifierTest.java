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

import org.junit.Rule;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.classdesign.TestUtils;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.exceptions.NoModifierException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

@RunWith(Theories.class)
public class ClassFieldModifierTest {

    private QfClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private FieldKeywordConfig field;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public", "private", "protected", ""};
    }

    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
            "static",
            "final",
            "transient",
            "volatile"
        };
    }

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


    private static void chooseAccessModifier(FieldKeywordConfig field, String accessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(choice));
        runnableMap.put("protected", () -> field.setProtectedModifier(choice));
        runnableMap.put("private", () -> field.setPrivateModifier(choice));
        runnableMap.put("", () -> field.setPackagePrivateModifier(choice));
        runnableMap.get(accessMod).run();
    }

    private static void chooseNonAccessModifier(FieldKeywordConfig field, String nonAccessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("static", () -> field.setStaticModifier(choice));
        runnableMap.put("final", () -> field.setFinalModifier(choice));
        runnableMap.put("transient", () -> field.setTransientModifier(choice));
        runnableMap.put("volatile", () -> field.setVolatileModifier(choice));
        runnableMap.put("", () -> field.setEmptyNonAccessModifier(choice));

        runnableMap.get(nonAccessMod).run();
    }

    private void init() {
        qfClassSettings = new QfClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        fieldKeywordConfigs = new ArrayList<>();
        field = new FieldKeywordConfig();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
    }

    private void setup() {
        field.setType("int");
        field.setName("a");
        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    @Theory
    public void dontCareTest(@FromDataPoints("accessModifiers") String correctMod,
                             @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb,
                              @FromDataPoints("exactMatching") Boolean isExactMatch) {

        init();
        field.setAllowExactModifierMatching(isExactMatch);

        String source = "class TestClass {" + correctMod;
        source += " "+String.join(" ", nonAccessComb);
        source += " int a;"+
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
    public void correctEmptyNonAccess(@FromDataPoints("accessModifiers") String correctMod,
                                      @FromDataPoints("accessModifiers") String wrongMod,
                                       @FromDataPoints("emptyModifier") String emptyNonAccess,
                                       @FromDataPoints("choices") String choice,
                                       @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }
        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
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
    public void faultEmptyNonAccess(@FromDataPoints("accessModifiers") String correctMod,
                                    @FromDataPoints("accessModifiers") String wrongMod,
                                    @FromDataPoints("emptyModifier") String emptyNonAccess,
                                    @FromDataPoints("choices") String choice,
                                    @FromDataPoints("exactMatching") Boolean isExactMatch) {
        assumeFalse(correctMod.equals(wrongMod));
        init();
        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }

        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
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
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
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
        chooseAccessModifier(field, correctMod, choice);
        field.setAllowExactModifierMatching(isExactMatch);


        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, nonAccess, choice);
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

        source += allowedAccess +" "+String.join(" ", allowedNonAccess) + " int a;}";

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
        field.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(field, correctMod, choice);
        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }

        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int a;}";
        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void nonAccessFault(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                               @FromDataPoints("choices") String choice,
                                               @FromDataPoints("exactMatching") Boolean isExactMatch) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();
        field.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }


        source += accessMod+" "+String.join(" ", allowedNonAccess);
        source += " int a;}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
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
        chooseAccessModifier(field, correctMod, choice);
        field.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        String allowedAccess = correctMod;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }


        source += allowedAccess+" "+String.join(" ", allowedNonAccess);
        source += " int a;}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback[] expectedFeedback = new ClassFeedback[] {fb};
        assertArrayEquals(expectedFeedback, classChecker.getClassFeedbacks().toArray(new ClassFeedback[0]));
    }

    @Theory
    public void multipleFieldsAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                            @FromDataPoints("accessModifiers") String wrongMod,
                            @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                            @FromDataPoints("choices") String choice,
                            @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();
        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field2.setAllowExactModifierMatching(isExactMatch);

        field.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(field, correctMod, choice);
        chooseAccessModifier(field2, correctMod, choice);

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }

        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int a;";
        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int b;";
        source += "}";

        field2.setType("int");
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
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void multipleFieldsNonAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                                           @FromDataPoints("accessModifiers") String wrongMod,
                                           @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                                           @FromDataPoints("choices") String choice,
                                           @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(correctNonAccess);

        init();
        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field2.setAllowExactModifierMatching(isExactMatch);
        field.setAllowExactModifierMatching(isExactMatch);

        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
            chooseNonAccessModifier(field2, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!isExactMatch && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        source += correctMod+" "+String.join(" ", allowedNonAccess) + " int a;";
        source += correctMod+" "+String.join(" ", allowedNonAccess) + " int b;";
        source += "}";

        field2.setType("int");
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
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void multipleAccessAndNonAccessFaults(@FromDataPoints("accessModifiers") String correctMod,
                                              @FromDataPoints("accessModifiers") String wrongMod,
                                              @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                                              @FromDataPoints("choices") String choice,
                                              @FromDataPoints("exactMatching") Boolean isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(correctNonAccess);

        init();
        FieldKeywordConfig field2 = new FieldKeywordConfig();
        field2.setAllowExactModifierMatching(isExactMatch);
        field.setAllowExactModifierMatching(isExactMatch);

        chooseAccessModifier(field, correctMod, choice);
        chooseAccessModifier(field2, correctMod, choice);
        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
            chooseNonAccessModifier(field2, nonAccess, choice);
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

        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int a;";
        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int b;";
        source += "}";

        field2.setType("int");
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
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void fewerExpectedFields() {
        init();
        FieldKeywordConfig field2 = new FieldKeywordConfig();

        chooseAccessModifier(field, "private", KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, "final", KeywordChoice.YES.toString());
        field.setType("int");
        field.setName("a");

        chooseAccessModifier(field2, "private", KeywordChoice.YES.toString());
        chooseNonAccessModifier(field2, "final", KeywordChoice.YES.toString());
        field2.setType("int");
        field2.setName("b");
        fieldKeywordConfigs.add(field2);

        String source = "class TestClass {";
        source += "public final int a;";
        source += "public static int b;";
        source += "public static int c;";
        source += "}";

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
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb3 = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.TOO_MANY_FIELDS);


        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2, fb3));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void moreExpectedFields() {
        init();
        FieldKeywordConfig field2 = new FieldKeywordConfig();
        FieldKeywordConfig field3 = new FieldKeywordConfig();

        chooseAccessModifier(field, "private", KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, "final", KeywordChoice.YES.toString());
        field.setType("int");
        field.setName("a");

        chooseAccessModifier(field2, "public", KeywordChoice.YES.toString());
        chooseNonAccessModifier(field2, "final", KeywordChoice.YES.toString());
        field2.setType("int");
        field2.setName("b");
        fieldKeywordConfigs.add(field2);


        chooseAccessModifier(field3, "private", KeywordChoice.YES.toString());
        chooseNonAccessModifier(field3, "final", KeywordChoice.YES.toString());
        field3.setType("int");
        field3.setName("c");
        fieldKeywordConfigs.add(field3);

        String source = "class TestClass {";
        source += "public final int a;";
        source += "public static int b;";
        source += "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassFeedback fb1 = TestUtils.getFeedback("class TestClass", "a", ClassFeedbackType.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = TestUtils.getFeedback("class TestClass", "b", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb3 = TestUtils.getFeedback("class TestClass", "", ClassFeedbackType.MISSING_FIELDS);

        HashSet<ClassFeedback> expectedFeedback = new HashSet<>(Arrays.asList(fb1, fb2, fb3));
        assertEquals(expectedFeedback, new HashSet<>(classChecker.getClassFeedbacks()));
    }

    @Theory
    public void noModifierException() {
        init();
        for (String access: accessValues()) {
            chooseAccessModifier(field, access, KeywordChoice.NO.toString());
        }
        for(String nonAccess: nonAccessValues()) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.NO.toString());
        }
        field.setEmptyNonAccessModifier(KeywordChoice.NO.toString());

        String source = "class TestClass {}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        NoModifierException e = Assertions.assertThrows(NoModifierException.class, () -> classChecker.check(null));
        assertEquals("At least one modifier has to be allowed.", e.getMessage());
    }
}
