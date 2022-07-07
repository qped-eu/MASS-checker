package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class FieldModifierTest {


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
    public static String emptyValue() {
        return "";
    }

    @DataPoints("allNonAccessModifierCombinations")
    public static String[][] allNonAccessValues() {
        String[] possibleNonAccess = nonAccessValues();
        List<String[]> combinationList = new ArrayList<>();
        // Start i at 1, so that we do not include the empty set in the results
        for (long i = 1; i < Math.pow(2, possibleNonAccess.length); i++ ) {
            List<String> portList = new ArrayList<>();
            for ( int j = 0; j < possibleNonAccess.length; j++ ) {
                if ( (i & (long) Math.pow(2, j)) > 0 ) {
                    // Include j in set
                    portList.add(possibleNonAccess[j]);
                }
            }
            combinationList.add(portList.toArray(new String[0]));
        }
        return combinationList.toArray(new String[0][0]);
    }

    @DataPoints("choices")
    public static String[] choiceValues() {
        return new String[] {KeywordChoice.YES.toString(), KeywordChoice.NO.toString()};
    }

    @DataPoints("exactMatching")
    public static String[] exactValues() {
        return new String[] {"true", "false"};
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


    //test access modifier
    //test non access modifier
    //test exact and inexact matching
    //test allowing and disallowing variables
    //test empty

    //test exact matching differently?

    //test multiple fields once?


    //test type
    //test name
    //test missing fields
    //test hidden?

    @Theory
    public void emptyNonAccessCorrect(@FromDataPoints("accessModifiers") String correctMod,
                                      @FromDataPoints("accessModifiers") String wrongMod,
                                           @FromDataPoints("emptyModifier") String emptyNonAccess,
                                           @FromDataPoints("choices") String choice,
                                           @FromDataPoints("exactMatching") String isExactMatch) {

        if(correctMod.equals(wrongMod)) return;
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }
        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
                "}";

        field.setFieldType("int");
        field.setName("a");

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

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
    public void emptyNonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                                    @FromDataPoints("accessModifiers") String wrongMod,
                                    @FromDataPoints("emptyModifier") String emptyNonAccess,
                                    @FromDataPoints("choices") String choice,
                                    @FromDataPoints("exactMatching") String isExactMatch) {
        if(correctMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }

        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
                "}";

        field.setFieldType("int");
        field.setName("a");

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

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

        if(correctMod.equals(wrongMod)) return;
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        chooseAccessModifier(field, correctMod, choice);
        field.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }
        field.setFieldType("int");
        field.setName("a");

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(nonAccessComb);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
        }

        source += allowedAccess +" "+String.join(" ", allowedNonAccess) + " int a;}";

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

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

        if(correctMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        chooseAccessModifier(field, correctMod, choice);
        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }
        String source = "class TestClass {";
        String allowedAccess = correctMod;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }
        source += allowedAccess+" "+String.join(" ", correctNonAccess) + " int a;}";

        field.setAllowExactModifierMatching(isExactMatch);
        field.setFieldType("int");
        field.setName("a");


        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

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
    public void nonAccessFault(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                               @FromDataPoints("choices") String choice,
                                               @FromDataPoints("exactMatching") String isExactMatch) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching(isExactMatch);
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        source += accessMod+" "+String.join(" ", allowedNonAccess);
        source += " int a;}";

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

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
