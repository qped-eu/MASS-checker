package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.apache.logging.log4j.util.Strings;
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


    private static void allowAccessModifier(FieldKeywordConfig field, String accessMod) {
        String yes = KeywordChoice.YES.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(yes));
        runnableMap.put("protected", () -> field.setProtectedModifier(yes));
        runnableMap.put("private", () -> field.setPrivateModifier(yes));
        runnableMap.put("", () -> field.setPackagePrivateModifier(yes));
        runnableMap.get(accessMod).run();
    }

    private static void allowNonAccessModifier(FieldKeywordConfig field, String nonAccessMod) {
        String yes = KeywordChoice.YES.toString();
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("static", () -> field.setStaticModifier(yes));
        runnableMap.put("final", () -> field.setFinalModifier(yes));
        runnableMap.put("transient", () -> field.setTransientModifier(yes));
        runnableMap.put("volatile", () -> field.setVolatileModifier(yes));
        runnableMap.put("", () -> field.setDefaultNonAccessModifier(yes));

        runnableMap.get(nonAccessMod).run();
    }

    private static void disAllowAccessModifier(FieldKeywordConfig field, String accessMod) {
        String no = KeywordChoice.NO.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(no));
        runnableMap.put("protected", () -> field.setProtectedModifier(no));
        runnableMap.put("private", () -> field.setPrivateModifier(no));
        runnableMap.put("", () -> field.setPackagePrivateModifier(no));
        runnableMap.get(accessMod).run();
    }

    private static void disAllowNonAccessModifier(FieldKeywordConfig field, String nonAccessMod) {
        String no = KeywordChoice.NO.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("static", () -> field.setStaticModifier(no));
        runnableMap.put("final", () -> field.setFinalModifier(no));
        runnableMap.put("transient", () -> field.setTransientModifier(no));
        runnableMap.put("volatile", () -> field.setVolatileModifier(no));
        runnableMap.put("", () -> field.setDefaultNonAccessModifier(no));

        runnableMap.get(nonAccessMod).run();
    }

    private static String getRandomAllowedAccess(List<String> modifiers, String disallowedMod) {
        String mod = disallowedMod;
        while(mod.equals(disallowedMod)) {
            int rnd = new Random().nextInt(modifiers.size());
            mod = modifiers.get(rnd);
        }
        return mod;
    }

    private static List<String> getRandomAllowedNonAccess(List<String> modifiers, List<String> disallowedNonAccess) {
        List<String> allowedMods = new ArrayList<>();
        for (String modifier : modifiers) {
            if(!disallowedNonAccess.contains(modifier)) {
                allowedMods.add(modifier);
            }
        }
        return allowedMods;
    }

    //test access modifier
    //test non access modifier
    //test exact and inexact matching
    //test allowing and disallowing variables
    //test empty


    //test type
    //test name
    //test missing fields
    //test hidden?



    @Theory
    public void inExactEmptyNonAccessNoFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                    @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        allowNonAccessModifier(field, emptyNonAccess);

        String source = "class TestClass {" + accessMod;
        source += " "+emptyNonAccess;
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
    public void inExactEmptyNonAccessFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                             @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        allowNonAccessModifier(field, emptyNonAccess);

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
    public void exactEmptyNonAccessNoFault(@FromDataPoints("accessModifiers") String accessMod,
                                    @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        allowNonAccessModifier(field, emptyNonAccess);
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        source += " "+emptyNonAccess;
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
    public void exactEmptyNonAccessNoFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                           @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        allowNonAccessModifier(field, emptyNonAccess);
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
    public void inExactEmptyNonAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                                      @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        disAllowNonAccessModifier(field, emptyNonAccess);

        String source = "class TestClass {" + accessMod;
        source += " "+emptyNonAccess;
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
    public void inExactEmptyNonAccessNoFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                                    @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        disAllowNonAccessModifier(field, emptyNonAccess);

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
    public void exactEmptyNonAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                           @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        disAllowNonAccessModifier(field, emptyNonAccess);
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        source += " "+emptyNonAccess;
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
    public void exactEmptyNonAccessNoFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                                  @FromDataPoints("emptyModifier") String emptyNonAccess) {
        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        disAllowNonAccessModifier(field, emptyNonAccess);
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
    public void inExactNoFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                    @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);

        String source = "class TestClass {" + accessMod;
        for (String nonAccess: nonAccessComb) {
            allowNonAccessModifier(field, nonAccess);
            source += " "+nonAccess;
        }
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
    public void exactNoFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                      @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        for (String nonAccess: nonAccessComb) {
            allowNonAccessModifier(field, nonAccess);
            source += " "+nonAccess;
        }
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
    public void inExactMatchAccessFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                                 @FromDataPoints("accessModifiers") String wrongMod,
                                                 @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessMods) {

        if(accessMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, wrongMod);
        field.setFieldType("int");
        field.setName("a");

        String source = "class TestClass {" +
                accessMod;
        for (String nonAccess: nonAccessMods) {
            source += " "+nonAccess;
            allowNonAccessModifier(field, nonAccess);
        }
        source += " int a;" +
                        "}";

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
    public void exactMatchAccessFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("accessModifiers") String wrongMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessMods) {

        if(accessMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, wrongMod);
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        String source = "class TestClass {" +
                accessMod;
        for (String nonAccess: nonAccessMods) {
            source += " "+nonAccess;
            allowNonAccessModifier(field, nonAccess);
        }
        source += " int a;" +
                "}";

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
    public void inExactNonAccessFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] actualNonAccess) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);
        List<String> actualList = Arrays.asList(actualNonAccess);

        if(expectedList.containsAll(actualList)) return;


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            allowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {"+accessMod;
        source += " "+String.join(" ", actualList);
        source += " int a;"+
                "}";


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
    public void exactNonAccessFaultAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                             @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                             @FromDataPoints("allNonAccessModifierCombinations") String[] actualNonAccess) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);
        List<String> actualList = new ArrayList<>(Arrays.asList(actualNonAccess));
        if(expectedList.containsAll(actualList) && actualList.containsAll(expectedList)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        allowAccessModifier(field, accessMod);
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            allowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {"+accessMod;
        source += " "+String.join(" ", actualList);
        source += " int a;"+
                "}";


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
    public void inExactNoFaultDisallowOnly(@FromDataPoints("accessModifiers") String disallowedAccess,
                                    @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, disallowedAccess);

        String allowedAccess = getRandomAllowedAccess(Arrays.asList(accessValues()), disallowedAccess);
        String source = "class TestClass {" + allowedAccess;
        for (String disallowedNonAccess: nonAccessComb) {
            disAllowNonAccessModifier(field, disallowedNonAccess);
        }
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
        source += " "+ String.join(" ", allowedNonAccess);
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
    public void exactNoFaultDisallowOnly(@FromDataPoints("accessModifiers") String disallowedAccess,
                                      @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, disallowedAccess);
        field.setAllowExactModifierMatching("true");

        String allowedAccess = getRandomAllowedAccess(Arrays.asList(accessValues()), disallowedAccess);
        String source = "class TestClass {" + allowedAccess;
        for (String disallowedNonAccess: nonAccessComb) {
            disAllowNonAccessModifier(field, disallowedNonAccess);
        }
        List<String> allowedNonAccess = getRandomAllowedNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
        source += " "+ String.join(" ", allowedNonAccess);
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
    public void inExactMatchAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                                 @FromDataPoints("accessModifiers") String wrongMod,
                                                 @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessMods) {

        if(accessMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, wrongMod);
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: nonAccessMods) {
            disAllowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {" + accessMod;
        source += " "+String.join(" ", nonAccessMods);
        source += " int a;" +
                "}";

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
    public void exactMatchAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("accessModifiers") String wrongMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessMods) {

        if(accessMod.equals(wrongMod)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, wrongMod);
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: nonAccessMods) {
            disAllowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {" + accessMod;
        source += " "+String.join(" ", nonAccessMods);
        source += " int a;" +
                "}";

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
    public void inExactNonAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] actualNonAccess) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);
        List<String> actualList = Arrays.asList(actualNonAccess);

        if(expectedList.containsAll(actualList)) return;


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, accessMod);
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            disAllowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {"+accessMod;
        for (String nonAccess: actualNonAccess) {
            source += " "+nonAccess;
        }
        source += " int a;"+
                "}";


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
    public void exactNonAccessFaultDisallowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                             @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                             @FromDataPoints("allNonAccessModifierCombinations") String[] actualNonAccess) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);
        List<String> actualList = Arrays.asList(actualNonAccess);
        if(expectedList.containsAll(actualList) && actualList.containsAll(expectedList)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classKeywordConfig.setClassType(KeywordChoice.YES.toString());
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        disAllowAccessModifier(field, accessMod);
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            disAllowNonAccessModifier(field, nonAccess);
        }

        String source = "class TestClass {"+accessMod;
        for (String nonAccess: actualNonAccess) {
            source += " "+nonAccess;
        }
        source += " int a;"+
                "}";


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
