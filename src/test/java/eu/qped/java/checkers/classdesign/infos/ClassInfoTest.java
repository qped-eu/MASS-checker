package eu.qped.java.checkers.classdesign.infos;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import eu.qped.java.checkers.classdesign.config.*;

import java.util.ArrayList;
import java.util.List;

class ClassInfoTest {
    @Test
    public void testGetClassKeywordConfig() {
        ClassInfo classInfo = new ClassInfo();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
        assertEquals(classKeywordConfig, classInfo.getClassKeywordConfig());
    }

    @Test
    public void testGetInheritsFromConfigs() {
        ClassInfo classInfo = new ClassInfo();
        List<InheritsFromConfig> inheritsFromConfigs = new ArrayList<>();
        classInfo.setInheritsFromConfigs(inheritsFromConfigs);
        assertEquals(inheritsFromConfigs, classInfo.getInheritsFromConfigs());
    }

    @Test
    public void testGetFieldKeywordConfigs() {
        ClassInfo classInfo = new ClassInfo();
        List<FieldKeywordConfig> fieldKeywordConfigs = new ArrayList<>();
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);
        assertEquals(fieldKeywordConfigs, classInfo.getFieldKeywordConfigs());
    }

    @Test
    public void testGetMethodKeywordConfigs() {
        ClassInfo classInfo = new ClassInfo();
        List<MethodKeywordConfig> methodKeywordConfigs = new ArrayList<>();
        classInfo.setMethodKeywordConfigs(methodKeywordConfigs);
        assertEquals(methodKeywordConfigs, classInfo.getMethodKeywordConfigs());
    }

    @Test
    public void testIsMatchExactFieldAmount() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setMatchExactFieldAmount(true);
        assertTrue(classInfo.isMatchExactFieldAmount());
    }

    @Test
    public void testIsMatchExactMethodAmount() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setMatchExactMethodAmount(true);
        assertTrue(classInfo.isMatchExactMethodAmount());
    }

    @Test
    public void testGetCustomFieldFeedback() {
        ClassInfo classInfo = new ClassInfo();
        List<String> customFieldFeedback = new ArrayList<>();
        classInfo.setCustomFieldFeedback(customFieldFeedback);
        assertEquals(customFieldFeedback, classInfo.getCustomFieldFeedback());
    }

    @Test
    public void testGetCustomMethodFeedback() {
        ClassInfo classInfo = new ClassInfo();
        List<String> customMethodFeedback = new ArrayList<>();
        classInfo.setCustomMethodFeedback(customMethodFeedback);
        assertEquals(customMethodFeedback, classInfo.getCustomMethodFeedback());
    }


}