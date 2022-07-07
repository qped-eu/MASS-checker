package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "",
        };
    }

    @DataPoints("reducedAccess")
    public static String[] abstractAccessValues() {
        return new String[] {
                "public",
                "protected",
                ""
        };
    }

    @DataPoints("reducedNonAccess")
    public static String[] reducedNonAccessValues() {
        return new String[] {
                "abstract"
        };
    }

    //test access modifier <- focus on this
    //test non access modifier


    //test type
    //test name
    //test missing
    //test override/overload?
    //test hidden?

    private static void setAccessModifier(MethodKeywordConfig method, String accessMod) {
        String yes = KeywordChoice.YES.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> method.setPublicModifier(yes));
        runnableMap.put("protected", () -> method.setProtectedModifier(yes));
        runnableMap.put("private", () -> method.setPrivateModifier(yes));
        runnableMap.put("", () -> method.setPackagePrivateModifier(yes));
        runnableMap.get(accessMod).run();
    }

    private static void setNonAccessModifier(MethodKeywordConfig method, String nonAccessMod) {
        String yes = KeywordChoice.YES.toString();
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("abstract", () -> method.setAbstractModifier(yes));
        runnableMap.put("static", () -> method.setStaticModifier(yes));
        runnableMap.put("final", () -> method.setFinalModifier(yes));
        runnableMap.put("default", () -> method.setDefaultModifier(yes));
        runnableMap.put("synchronized", () -> method.setSynchronizedModifier(yes));
        runnableMap.put("native", () -> method.setNativeModifier(yes));
        runnableMap.put("", () -> {});


        runnableMap.get(nonAccessMod).run();
    }


    @Theory
    public void abstractModifierCombinations(@FromDataPoints("reducedAccess") String accessMod,
                                             @FromDataPoints("reducedNonAccess") String nonAccessMod) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();

        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        String classType = "class";
        if(nonAccessMod.equals("default")) {
            classType = "interface";
            classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
            classKeywordConfig.setClassType(KeywordChoice.DONTCARE.toString());
        }

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        setAccessModifier(method, accessMod);
        setNonAccessModifier(method, nonAccessMod);
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
    public void modifierCombinations(@FromDataPoints("accessModifiers") String accessMod,
                                     @FromDataPoints("nonAccessModifiers") String nonAccessMod) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();

        String classType = "class";
        if(nonAccessMod.equals("default")) {
            classType = "interface";
            classKeywordConfig.setInterfaceType(KeywordChoice.YES.toString());
            classKeywordConfig.setClassType(KeywordChoice.DONTCARE.toString());
        }

        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        MethodKeywordConfig method = new MethodKeywordConfig();
        setAccessModifier(method, accessMod);
        setNonAccessModifier(method, nonAccessMod);
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
}
