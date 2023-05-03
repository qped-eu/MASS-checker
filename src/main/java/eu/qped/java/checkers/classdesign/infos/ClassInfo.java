package eu.qped.java.checkers.classdesign.infos;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.InheritsFromConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;

import java.util.ArrayList;
import java.util.List;

/**
 The ClassInfo class encapsulates different pieces of information describing the feedback parameters for a class.
 It contains fields for storing class keyword configuration, inherits from configurations, field keyword configurations,
 method keyword configurations, match exact field and method amount, custom feedback for fields, methods, class and inheritance.
 @author [Author Name]
 */
public class ClassInfo {

    /**
     The fully qualified name of the class.
     */
     private String fullyQualifiedName;
     /**
     The ClassKeywordConfig object containing the class keyword configurations.
     */
     private ClassKeywordConfig classKeywordConfig;
     /**
     The list of InheritsFromConfig objects containing the inherits from configurations.
     */
     private List<InheritsFromConfig> inheritsFromConfigs;
     /**
     The list of FieldKeywordConfig objects containing the field keyword configurations.
     */
     private List<FieldKeywordConfig> fieldKeywordConfigs;
     /**
     The list of MethodKeywordConfig objects containing the method keyword configurations.
     */
    private List<MethodKeywordConfig> methodKeywordConfigs;
    /**
     A flag indicating whether the exact field amount should be matched or not.
     */
     private boolean matchExactFieldAmount;
     /**
     A flag indicating whether the exact method amount should be matched or not.
     */
    private boolean matchExactMethodAmount;
    /**
     A list of custom feedback strings for fields.
     */
     private List<String> customFieldFeedback;
     /**
     A list of custom feedback strings for methods.
     */
     private List<String> customMethodFeedback;
     /**
     A list of custom feedback strings for class.
     */
     private List<String> customClassFeedback;
     /**
     A list of custom feedback strings for inheritance.
     */
    private List<String> customInheritanceFeedback;
    /**
     Class constructor. Initializes all fields with default values.
     */
    public ClassInfo() {
        fullyQualifiedName = "";

        classKeywordConfig = new ClassKeywordConfig();
        inheritsFromConfigs = new ArrayList<>();
        fieldKeywordConfigs = new ArrayList<>();
        methodKeywordConfigs = new ArrayList<>();

        matchExactFieldAmount = false;
        matchExactMethodAmount = false;

        customFieldFeedback = new ArrayList<>();
        customMethodFeedback = new ArrayList<>();
        customClassFeedback = new ArrayList<>();
        customInheritanceFeedback = new ArrayList<>();
    }

    /**
     A getter method for the classKeywordConfig field.
     @return the current value of the variable classKeywordConfig.
     */
    public ClassKeywordConfig getClassKeywordConfig() {
        return classKeywordConfig;
    }
    /**
     A setter method for classKeywordConfig.
     @param classKeywordConfig the value to be assigned.
     */
    public void setClassKeywordConfig(final ClassKeywordConfig classKeywordConfig) {
        this.classKeywordConfig = classKeywordConfig;
    }

    /**
     A getter method for the inheritsFromConfigs field.
     @return a list of InheritsFromConfig objects containing the inherits from configurations.
     */
    public List<InheritsFromConfig> getInheritsFromConfigs() {
        return inheritsFromConfigs;
    }

    /**
     A setter method for inheritsFromConfigs field.
     @param inheritsFromConfigs list of InheritsFromConfig objects to be assigned to the inheritsFromConfigs field.
     */
    public void setInheritsFromConfigs(final List<InheritsFromConfig> inheritsFromConfigs) {
        this.inheritsFromConfigs = inheritsFromConfigs;
    }

    /**
     A getter method for the fieldKeywordConfigs field.
     @return a list of FieldKeywordConfig objects containing the field keyword configurations.
     */
    public List<FieldKeywordConfig> getFieldKeywordConfigs() {
        return fieldKeywordConfigs;
    }

    /**
     A setter method for fieldKeywordConfigs field.
     @param fieldKeywords list of FieldKeywordConfig objects to be assigned to the fieldKeywordConfigs field.
     */
    public void setFieldKeywordConfigs(final List<FieldKeywordConfig> fieldKeywords) {
        this.fieldKeywordConfigs = fieldKeywords;
    }

    /**
     A getter method for the methodKeywordConfigs field.
     @return a list of MethodKeywordConfig objects containing the method keyword configurations.
     */
    public List<MethodKeywordConfig> getMethodKeywordConfigs() {
        return methodKeywordConfigs;
    }

    /**
     A setter method for the methodKeywordConfigs field.
     @param methodKeywords list of MethodKeywordConfig objects to be assigned to the methodKeywordConfigs field.
     */
    public void setMethodKeywordConfigs(final List<MethodKeywordConfig> methodKeywords) {
        this.methodKeywordConfigs = methodKeywords;
    }

    /**
     A getter method for the customFieldFeedback field.
     @return a list of strings containing custom feedback for fields.
     */
    public List<String> getCustomFieldFeedback() {
        return customFieldFeedback;
    }

    /**
     A setter method for the customFieldFeedback field.
     @param customFieldFeedback list of strings to be assigned to the customFieldFeedback field.
     */
    public void setCustomFieldFeedback(final List<String> customFieldFeedback) {
        this.customFieldFeedback = customFieldFeedback;
    }

    /**
     A getter method for the customMethodFeedback field.
     @return a list of strings containing custom feedback for methods.
     */
    public List<String> getCustomMethodFeedback() {
        return customMethodFeedback;
    }

    /**
     A setter method for the customMethodFeedback field.
     @param customMethodFeedback list of strings to be assigned to the customMethodFeedback field.
     */
    public void setCustomMethodFeedback(final List<String> customMethodFeedback) {
        this.customMethodFeedback = customMethodFeedback;
    }

    /**
     A getter method for the customClassFeedback field.
     @return a list of strings containing custom feedback for the class.
     */
    public List<String> getCustomClassFeedback() {
        return customClassFeedback;
    }

    /**
     A setter method for the customClassFeedback field.
     @param customClassFeedback list of strings to be assigned to the customClassFeedback field.
     */
    public void setCustomClassFeedback(final List<String> customClassFeedback) {
        this.customClassFeedback = customClassFeedback;
    }

    /**
     A getter method for the customInheritanceFeedback field.
     @return a list of strings containing custom feedback for the class inheritance.
     */
    public List<String> getCustomInheritanceFeedback() {
        return customInheritanceFeedback;
    }

    /**
     A setter method for the customInheritanceFeedback field.
     @param customInheritanceFeedback list of strings to be assigned to the customInheritanceFeedback field.
     */
    public void setCustomInheritanceFeedback(final List<String> customInheritanceFeedback) {
        this.customInheritanceFeedback = customInheritanceFeedback;
    }

    /**
     A getter method for the fullyQualifiedName field.
     @return the fully qualified name of the class.
     */
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    /**
     A setter method for the fullyQualifiedName field.
     @param fullyQualifiedName the fully qualified name of the class to be assigned to the fullyQualifiedName field.
     */
    public void setFullyQualifiedName(final String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    /**
     A getter method for the matchExactFieldAmount field.
     @return a boolean indicating if the expected number of fields should match exactly or not.
     */
    public boolean isMatchExactFieldAmount() {
        return matchExactFieldAmount;
    }

    /**
     A setter method for the matchExactFieldAmount field.
     @param matchExactFieldAmount boolean value indicating if the expected number of fields should match exactly or not.
     */
    public void setMatchExactFieldAmount(final boolean matchExactFieldAmount) {
        this.matchExactFieldAmount = matchExactFieldAmount;
    }

    /**
     A getter method for the matchExactMethodAmount field.
     @return a boolean indicating if the expected number of methods should match exactly or not.
     */
    public boolean isMatchExactMethodAmount() {
        return matchExactMethodAmount;
    }

    /**
     A setter method for the matchExactMethodAmount field.
     @param matchExactMethodAmount boolean value indicating if the expected number of methods should match exactly or not.
     */
    public void setMatchExactMethodAmount(final boolean matchExactMethodAmount) {
        this.matchExactMethodAmount = matchExactMethodAmount;
    }
}
