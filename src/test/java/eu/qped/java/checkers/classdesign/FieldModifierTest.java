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

    //test multiple fields once?


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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.YES.toString());

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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.YES.toString());

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.NO.toString());

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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.NO.toString());

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.NO.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(field, emptyNonAccess, KeywordChoice.NO.toString());
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Collections.singletonList(emptyNonAccess));
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());

        String source = "class TestClass {" + accessMod;
        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching("true");

        String source = "class TestClass {" + accessMod;
        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, wrongMod, KeywordChoice.YES.toString());
        field.setFieldType("int");
        field.setName("a");

        String source = "class TestClass {" +
                accessMod;
        for (String nonAccess: nonAccessMods) {
            source += " "+nonAccess;
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, wrongMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        String source = "class TestClass {" +
                accessMod;
        for (String nonAccess: nonAccessMods) {
            source += " "+nonAccess;
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

//        FieldKeywordConfig field2 = new FieldKeywordConfig();
//        allowAccessModifier(field2, accessMod);
//        field2.setAllowExactModifierMatching("true");
//        field2.setFieldType("int");
//        field2.setName("b");

        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.YES.toString());
//            allowNonAccessModifier(field2, nonAccess);
        }

        String source = "class TestClass {";
        String fieldDecl = accessMod+" "+String.join(" ", actualList)+" int a;";
        //String fieldDecl2 = accessMod+" "+String.join(" ", actualList)+" int b;";
        source += fieldDecl+ "}";

        //fieldKeywordConfigs.add(field2);
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
        chooseAccessModifier(field, disallowedAccess, KeywordChoice.NO.toString());

        String allowedAccess = CheckerUtils.getRandomAllowedAccess(Arrays.asList(accessValues()), disallowedAccess);
        String source = "class TestClass {" + allowedAccess;
        for (String disallowedNonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, disallowedNonAccess, KeywordChoice.NO.toString());
        }
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
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
        chooseAccessModifier(field, disallowedAccess, KeywordChoice.NO.toString());
        field.setAllowExactModifierMatching("true");

        String allowedAccess = CheckerUtils.getRandomAllowedAccess(Arrays.asList(accessValues()), disallowedAccess);
        String source = "class TestClass {" + allowedAccess;
        for (String disallowedNonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, disallowedNonAccess, KeywordChoice.NO.toString());
        }
        List<String> allowedNonAccess = CheckerUtils.getAllowedNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
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
        chooseAccessModifier(field, wrongMod, KeywordChoice.NO.toString());
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: nonAccessMods) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.NO.toString());
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
        chooseAccessModifier(field, wrongMod, KeywordChoice.NO.toString());
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: nonAccessMods) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.NO.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.NO.toString());
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.NO.toString());
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
        chooseAccessModifier(field, accessMod, KeywordChoice.NO.toString());
        field.setAllowExactModifierMatching("true");
        field.setFieldType("int");
        field.setName("a");

        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, KeywordChoice.NO.toString());
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
