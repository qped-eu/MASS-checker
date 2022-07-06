package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "volatile",
                ""
        };
    }


    private static void setAccessModifier(FieldKeywordConfig field, String accessMod) {
        String yes = KeywordChoice.YES.toString();

        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(yes));
        runnableMap.put("protected", () -> field.setProtectedModifier(yes));
        runnableMap.put("private", () -> field.setPrivateModifier(yes));
        runnableMap.put("", () -> field.setPackagePrivateModifier(yes));
        runnableMap.get(accessMod).run();
    }

    private static void setNonAccessModifier(FieldKeywordConfig field, String nonAccessMod) {
        String yes = KeywordChoice.YES.toString();
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("abstract", () -> field.setAbstractModifier(yes));
        runnableMap.put("static", () -> field.setStaticModifier(yes));
        runnableMap.put("final", () -> field.setFinalModifier(yes));
        runnableMap.put("transient", () -> field.setTransientModifier(yes));
        runnableMap.put("volatile", () -> field.setVolatileModifier(yes));
        runnableMap.put("", () -> {});

        runnableMap.get(nonAccessMod).run();
    }




    @Theory
    public void modifierCombinations(@FromDataPoints("accessModifiers") String accessMod,
                                     @FromDataPoints("nonAccessModifiers") String firstNonAccess,
                                     @FromDataPoints("nonAccessModifiers") String secondNonAccess) {

        if(firstNonAccess.equals(secondNonAccess)) return;

        QFClassSettings qfClassSettings = new QFClassSettings();
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);

        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        FieldKeywordConfig field = new FieldKeywordConfig();
        setAccessModifier(field, accessMod);
        setNonAccessModifier(field, firstNonAccess);
        setNonAccessModifier(field, secondNonAccess);
        field.setFieldType("int");
        field.setName("a");

        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                accessMod +
                " " + firstNonAccess +
                " " + secondNonAccess +
                " int a;" +
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
