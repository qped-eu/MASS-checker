package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class MethodModifierTest {

    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public", "private", "protected", ""};
    }

    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
                "default",
                "static",
                "final",
                "synchronized",
                "native",
//                "",
        };
    }

    @DataPoints("reducedAccess")
    public static String[] abstractAccessValues() {
        return new String[] {"public", "protected", ""};
    }

    @DataPoint("abstractKeyword")
    public static String abstractKeyword() {
        return "abstract";
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


    @Theory
    public void inExactAbstractAllowOnly(@FromDataPoints("reducedAccess") String accessMod,
                                     @FromDataPoints("reducedNonAccess") String nonAccessMod) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();

        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        chooseAccessModifier(method, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(method, nonAccessMod, KeywordChoice.YES.toString());
        method.setMethodType("int");
        method.setName("test");

        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                accessMod +
                " "
                + nonAccessMod +
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
    public void exactAbstractAllowOnly(@FromDataPoints("reducedAccess") String accessMod,
                                                @FromDataPoints("reducedNonAccess") String nonAccessMod) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();

        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        String classType = "class";

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        chooseAccessModifier(method, accessMod, KeywordChoice.YES.toString());
        chooseNonAccessModifier(method, nonAccessMod, KeywordChoice.YES.toString());
        method.setAllowExactModifierMatching("true");
        method.setMethodType("int");
        method.setName("test");

        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = classType+" TestClass {" +
                accessMod +
                " "
                + nonAccessMod +
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
    public void inExactCorrectAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                     @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessMod) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();


        List<String> nonAccessList = Arrays.asList(nonAccessMod);
        String classType = "class";
        if(nonAccessList.contains("default")) {
            classType = "interface";
            classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
            classKeywordConfig.setClassType(KeywordChoice.DONTCARE.toString());
        }

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        chooseAccessModifier(method, accessMod, KeywordChoice.YES.toString());
        for (String nonAccess: nonAccessList) {
            chooseNonAccessModifier(method, nonAccess, KeywordChoice.YES.toString());
        }
        method.setMethodType("int");
        method.setName("test");

        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = classType+" TestClass {";
        source += accessMod + " " + String.join(" ", nonAccessMod) + " int test() {}" + "}";

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
    public void inExactWrongAllowOnly(@FromDataPoints("accessModifiers") String accessMod,
                                      @FromDataPoints("allNonAccessModifierCombinations") String[] expectedMods,
                                      @FromDataPoints("allNonAccessModifierCombinations") String[] actualMods) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();


        List<String> expectedList = Arrays.asList(expectedMods);
        List<String> actualList = Arrays.asList(actualMods);

        if(expectedList.containsAll(actualList)) return;

        String classType = "class";
        if(actualList.contains("default")) {
            classType = "interface";
            classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
            classKeywordConfig.setClassType(KeywordChoice.DONTCARE.toString());
        }

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        chooseAccessModifier(method, accessMod, KeywordChoice.YES.toString());
        for (String nonAccess: expectedList) {
            chooseNonAccessModifier(method, nonAccess, KeywordChoice.YES.toString());
        }
        method.setMethodType("int");
        method.setName("test");

        methodKeywordConfigs.add(method);
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = classType+" TestClass {";
        source += accessMod + " " + String.join(" ", actualList) + " int test() {}" + "}";

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
