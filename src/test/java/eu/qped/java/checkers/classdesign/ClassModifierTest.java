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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class ClassModifierTest {

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


    //test access modifier
    //test non access modifier


    //test type
    //test name

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

    @Theory
    public void innerClassCorrect(@FromDataPoints("innerClassAccess") String accessMod,
                                           @FromDataPoints("innerClassNonAccess") String nonAccessMod,
                                            @FromDataPoints("choices") String choice) {

        if(nonAccessMod.equals("abstract") && (accessMod.equals("private") || accessMod.equals("protected"))) return;


        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo outerClassInfo = new ClassInfo();
        ClassKeywordConfig outerClassConfig = new ClassKeywordConfig();
        outerClassInfo.setClassKeywordConfig(outerClassConfig);

        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, accessMod, choice);
        setNonAccessModifier(innerClassConfig, nonAccessMod, choice);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(outerClassInfo);
        classInfos.add(innerClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = " class TestClass {" +
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
        if(choice.equals(KeywordChoice.NO.toString())) {
            assertEquals(1, classChecker.getClassFeedbacks().size());
        } else {
            assertEquals(0, classChecker.getClassFeedbacks().size());
        }
    }

    @Theory
    public void innerClassWrong(@FromDataPoints("innerClassAccess") String correctAccess,
                                            @FromDataPoints("innerClassAccess") String wrongAccess,
                                            @FromDataPoints("innerClassNonAccess") String correctNonAccess,
                                            @FromDataPoints("innerClassNonAccess") String wrongNonAccess,
                                            @FromDataPoints("choices") String choice) {

        if(correctAccess.equals(wrongAccess) || correctNonAccess.equals(wrongNonAccess)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo outerClassInfo = new ClassInfo();
        ClassKeywordConfig outerClassConfig = new ClassKeywordConfig();
        outerClassInfo.setClassKeywordConfig(outerClassConfig);

        ClassInfo innerClassInfo = new ClassInfo();
        ClassKeywordConfig innerClassConfig = new ClassKeywordConfig();
        innerClassConfig.setName("InnerClass");
        setAccessModifier(innerClassConfig, correctAccess, choice);
        setNonAccessModifier(innerClassConfig, correctNonAccess, choice);
        innerClassInfo.setClassKeywordConfig(innerClassConfig);

        classInfos.add(outerClassInfo);
        classInfos.add(innerClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = " class TestClass {" +
                wrongAccess+" "+String.join(" ", wrongNonAccess)+" class InnerClass {}"+
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(choice.equals(KeywordChoice.NO.toString())) {
            assertEquals(0, classChecker.getClassFeedbacks().size());
        } else {
            assertEquals(1, classChecker.getClassFeedbacks().size());
        }
    }

    @Theory
    public void outerClassCorrect(@FromDataPoints("outerClassAccess") String accessMod,
                                        @FromDataPoints("nonAccessModifiers") String firstNonAccess,
                                        @FromDataPoints("choices") String choice) {

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        setAccessModifier(classKeywordConfig, accessMod, choice);
        setNonAccessModifier(classKeywordConfig, firstNonAccess, choice);
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
        if(choice.equals(KeywordChoice.NO.toString())) {
            assertEquals(1, classChecker.getClassFeedbacks().size());
        } else {
            assertEquals(0, classChecker.getClassFeedbacks().size());
        }

    }

    @Theory
    public void outerClassWrong(@FromDataPoints("outerClassAccess") String correctAccess,
                                @FromDataPoints("outerClassAccess") String wrongAccess,
                                @FromDataPoints("nonAccessModifiers") String correctNonAccess,
                                @FromDataPoints("nonAccessModifiers") String wrongNonAccess,
                                @FromDataPoints("choices") String choice) {

        if(correctAccess.equals(wrongAccess) || correctNonAccess.equals(wrongNonAccess)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        setAccessModifier(classKeywordConfig, correctAccess, choice);
        setNonAccessModifier(classKeywordConfig, correctNonAccess, choice);
        classInfo.setClassKeywordConfig(classKeywordConfig);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = wrongAccess +" "+ wrongNonAccess +" class TestClass {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(choice.equals(KeywordChoice.NO.toString())) {
            assertEquals(0, classChecker.getClassFeedbacks().size());
        } else {
            assertEquals(1, classChecker.getClassFeedbacks().size());
        }


    }

}
