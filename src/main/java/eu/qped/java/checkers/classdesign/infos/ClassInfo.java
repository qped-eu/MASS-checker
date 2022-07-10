package eu.qped.java.checkers.classdesign.infos;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.InheritsFromConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    private String fullyQualifiedName;
    private ClassKeywordConfig classKeywordConfig;
    private List<InheritsFromConfig> inheritsFrom;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private List<MethodKeywordConfig> methodKeywordConfigs;

    private String matchExactFieldAmount;
    private String matchExactMethodAmount;

    private List<String> customFieldFeedback;
    private List<String> customMethodFeedback;
    private List<String> customClassFeedback;
    private List<String> customInheritanceFeedback;


    public ClassInfo() {
        fullyQualifiedName = "";

        classKeywordConfig = new ClassKeywordConfig();
        inheritsFrom = new ArrayList<>();
        fieldKeywordConfigs = new ArrayList<>();
        methodKeywordConfigs = new ArrayList<>();

        matchExactFieldAmount = "false";
        matchExactMethodAmount = "false";

        customFieldFeedback = new ArrayList<>();
        customMethodFeedback = new ArrayList<>();
        customClassFeedback = new ArrayList<>();
        customInheritanceFeedback = new ArrayList<>();
    }



    public ClassKeywordConfig getClassKeywordConfig() {
        return classKeywordConfig;
    }

    public void setClassKeywordConfig(ClassKeywordConfig classKeywordConfig) {
        this.classKeywordConfig = classKeywordConfig;
    }

    public List<InheritsFromConfig> getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(List<InheritsFromConfig> inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public List<FieldKeywordConfig> getFieldKeywordConfigs() {
        return fieldKeywordConfigs;
    }

    public void setFieldKeywordConfigs(List<FieldKeywordConfig> fieldKeywords) {
        this.fieldKeywordConfigs = fieldKeywords;
    }

    public List<MethodKeywordConfig> getMethodKeywordConfigs() {
        return methodKeywordConfigs;
    }

    public void setMethodKeywordConfigs(List<MethodKeywordConfig> methodKeywords) {
        this.methodKeywordConfigs = methodKeywords;
    }

    public String getMatchExactMethodAmount() {
        return matchExactMethodAmount;
    }

    public void setMatchExactMethodAmount(String matchExactMethodAmount) {
        this.matchExactMethodAmount = matchExactMethodAmount;
    }

    public List<String> getCustomFieldFeedback() {
        return customFieldFeedback;
    }

    public void setCustomFieldFeedback(List<String> customFieldFeedback) {
        this.customFieldFeedback = customFieldFeedback;
    }

    public List<String> getCustomMethodFeedback() {
        return customMethodFeedback;
    }

    public void setCustomMethodFeedback(List<String> customMethodFeedback) {
        this.customMethodFeedback = customMethodFeedback;
    }

    public List<String> getCustomClassFeedback() {
        return customClassFeedback;
    }

    public void setCustomClassFeedback(List<String> customClassFeedback) {
        this.customClassFeedback = customClassFeedback;
    }

    public List<String> getCustomInheritanceFeedback() {
        return customInheritanceFeedback;
    }

    public void setCustomInheritanceFeedback(List<String> customInheritanceFeedback) {
        this.customInheritanceFeedback = customInheritanceFeedback;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getMatchExactFieldAmount() {
        return matchExactFieldAmount;
    }

    public void setMatchExactFieldAmount(String matchExactFieldAmount) {
        this.matchExactFieldAmount = matchExactFieldAmount;
    }
}
