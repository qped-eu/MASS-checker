package eu.qped.java.checkers.classdesign;

import com.fasterxml.jackson.databind.type.ClassKey;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.KeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class ClassModifierTest {

    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public",
                "private",
                "protected", ""};
    }

    @DataPoints("reducedAccess")
    public static String[] abstractAccessValues() {
        return new String[] {
                "public",
                ""
        };
    }


    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
                "abstract",
                "final",
                ""
        };
    }


    @DataPoint("staticKeyword")
    public static String staticValue() {
        return "static";
    }


    private static void setAccessModifier(KeywordConfig field, String accessMod) {
        String yes = KeywordChoice.YES.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(yes));
        runnableMap.put("protected", () -> field.setProtectedModifier(yes));
        runnableMap.put("private", () -> field.setPrivateModifier(yes));
        runnableMap.put("", () -> field.setPackagePrivateModifier(yes));
        runnableMap.get(accessMod).run();
    }

    private static void setNonAccessModifier(KeywordConfig field, String nonAccessMod) {
        String yes = KeywordChoice.YES.toString();
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("abstract", () -> field.setAbstractModifier(yes));
        runnableMap.put("static", () -> field.setStaticModifier(yes));
        runnableMap.put("final", () -> field.setFinalModifier(yes));
        runnableMap.put("", () -> {});

        runnableMap.get(nonAccessMod).run();
    }

    @Theory
    public void innerClassCombinations(@FromDataPoints("reducedAccess") String accessMod,
                                           @FromDataPoints("nonAccessModifiers") String nonAccessMod) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo outerClassInfo = new ClassInfo();
        ClassKeywordConfig outerClassConfig = new ClassKeywordConfig();
        setAccessModifier(outerClassConfig, accessMod);
        outerClassInfo.setClassKeywordConfig(outerClassConfig);

        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, accessMod);
        setNonAccessModifier(innerClassConfig, nonAccessMod);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(outerClassInfo);
        classInfos.add(innerClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = accessMod+" class TestClass {" +
                accessMod+" "+nonAccessMod+" class InnerClass {}"+
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
    public void staticModifierCombinations(@FromDataPoints("reducedAccess") String accessMod,
                                           @FromDataPoints("staticKeyword") String staticKeyword) {


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo outerClassInfo = new ClassInfo();
        ClassKeywordConfig outerClassConfig = new ClassKeywordConfig();
        setAccessModifier(outerClassConfig, accessMod);
        outerClassInfo.setClassKeywordConfig(outerClassConfig);

        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, accessMod);
        setNonAccessModifier(innerClassConfig, staticKeyword);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(outerClassInfo);
        classInfos.add(innerClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = accessMod+" class TestClass {" +
                accessMod+" "+staticKeyword+" class InnerClass {}"+
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
    public void modifierCombinations(@FromDataPoints("reducedAccess") String accessMod,
                                     @FromDataPoints("nonAccessModifiers") String firstNonAccess) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        setAccessModifier(classKeywordConfig, accessMod);
        setNonAccessModifier(classKeywordConfig, firstNonAccess);
        classInfo.setClassKeywordConfig(classKeywordConfig);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = accessMod +" "+ firstNonAccess +" class TestClass {" +
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
