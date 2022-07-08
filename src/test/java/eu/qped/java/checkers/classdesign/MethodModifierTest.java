package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class MethodModifierTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<MethodKeywordConfig> methodKeywordConfigs;
    private MethodKeywordConfig method;

    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public", "private", "protected", ""};
    }

    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
                "static",
                "final",
                "synchronized",
                "native",
        };
    }

    @DataPoints("reducedAccess")
    public static String[] abstractAccessValues() {
        return new String[] {"public", "protected", ""};
    }

    @DataPoint("abstractKeyword")
    public static String abstractKeyword = "abstract";

    @DataPoint("defaultKeyword")
    public static String defaultKeyword = "default";

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
    public static String[] exactValues() {
        return new String[] {"true", "false"};
    }



    //test access modifier
    //test non access modifier

    //test type
    //test name
    //test missing
    //test override/overload?
    //test hidden?

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
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        methodKeywordConfigs = new ArrayList<>();
        method = new MethodKeywordConfig();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
    }

    private void setup() {
        method.setType("int");
        method.setName("test");
        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    @Theory
    public void defaultCorrect(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("defaultKeyword") String nonAccessMod,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        method.setAllowExactModifierMatching(isExactMatch);

        setup();
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(nonAccessMod);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
    public void defaultFault(@FromDataPoints("reducedAccess") String correctMod,
                               @FromDataPoints("reducedAccess") String wrongMod,
                               @FromDataPoints("defaultKeyword") String nonAccessMod,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = wrongMod;
        List<String> allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = correctMod;
            allowedNonAccess = Collections.singletonList(nonAccessMod);
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }



    @Theory
    public void abstractCorrect(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("abstractKeyword") String nonAccessMod,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();

        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, nonAccessMod, choice);
        chooseNonAccessModifier(method, defaultKeyword, KeywordChoice.NO.toString());
        method.setAllowExactModifierMatching(isExactMatch);

        setup();

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(nonAccessMod);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(nonAccessMod));
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
    public void abstractFault(@FromDataPoints("reducedAccess") String correctMod,
                                @FromDataPoints("reducedAccess") String wrongMod,
                                @FromDataPoints("abstractKeyword") String nonAccessMod,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") String isExactMatch) {

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
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }


    @Theory
    public void modifierCorrect(@FromDataPoints("accessModifiers") String correctMod,
                                @FromDataPoints("accessModifiers") String wrongMod,
                                @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        chooseNonAccessModifier(method, defaultKeyword, KeywordChoice.NO.toString());
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
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
                                @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        chooseNonAccessModifier(method, defaultKeyword, KeywordChoice.NO.toString());
        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
        }

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void nonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                            @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                            @FromDataPoints("choices") String choice,
                            @FromDataPoints("exactMatching") String isExactMatch) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        chooseNonAccessModifier(method, defaultKeyword, KeywordChoice.NO.toString());
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(method, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void accessAndNonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                               @FromDataPoints("accessModifiers") String wrongMod,
                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();

        method.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(method, correctMod, choice);
        chooseNonAccessModifier(method, abstractKeyword, KeywordChoice.NO.toString());
        chooseNonAccessModifier(method, defaultKeyword, KeywordChoice.NO.toString());
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
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
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
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

}
