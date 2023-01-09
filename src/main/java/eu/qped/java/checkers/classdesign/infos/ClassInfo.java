package eu.qped.java.checkers.classdesign.infos;

import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.InheritsFromConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that encapsulates different pieces of information
 * describing the different feedback parameters
 */
public class ClassInfo {

    private String fullyQualifiedName;
    private ClassKeywordConfig classKeywordConfig;
    private List<InheritsFromConfig> inheritsFromConfigs;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private List<MethodKeywordConfig> methodKeywordConfigs;

    private boolean matchExactFieldAmount;
    private boolean matchExactMethodAmount;

    private List<String> customFieldFeedback;
    private List<String> customMethodFeedback;
    private List<String> customClassFeedback;
    private List<String> customInheritanceFeedback;


    /**
     * Class constructor
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
     * A getter method for the classKeywordConfig field
     * @return the current value of the variable classKeywordConfig
     */
    public ClassKeywordConfig getClassKeywordConfig() {
        return classKeywordConfig;
    }

    /**
     * A setter method for classKeywordConfig
     * @param classKeywordConfig the value to be assigned
     */
    public void setClassKeywordConfig(final ClassKeywordConfig classKeywordConfig) {
        this.classKeywordConfig = classKeywordConfig;
    }

    /**
     * A getter method for the InheritsFromConfigs field
     * @return the current value of the variable InheritsFromConfigs
     */
    public List<InheritsFromConfig> getInheritsFromConfigs() {
        return inheritsFromConfigs;
    }

    /**
     * A setter method for inheritsFromConfigs
     * @param inheritsFromConfigs the value to be assigned
     */
    public void setInheritsFromConfigs(final List<InheritsFromConfig> inheritsFromConfigs) {
        this.inheritsFromConfigs = inheritsFromConfigs;
    }

    /**
     * A getter method for the fieldKeywordConfigs field
     * @return the current value of the variable fieldKeywordConfigs
     */
    public List<FieldKeywordConfig> getFieldKeywordConfigs() {
        return fieldKeywordConfigs;
    }

    /**
     * A setter method for inheritsFromConfigs
     * @param fieldKeywords the value to be assigned
     */
    public void setFieldKeywordConfigs(final List<FieldKeywordConfig> fieldKeywords) {
        this.fieldKeywordConfigs = fieldKeywords;
    }

    /**
     * A getter method for the methodKeywordConfigs field
     * @return the current value of the variable methodKeywordConfigs
     */
    public List<MethodKeywordConfig> getMethodKeywordConfigs() {
        return methodKeywordConfigs;
    }

    /**
     * A setter method for methodKeywords
     * @param methodKeywords the value to be assigned
     */
    public void setMethodKeywordConfigs(final List<MethodKeywordConfig> methodKeywords) {
        this.methodKeywordConfigs = methodKeywords;
    }

    /**
     * A getter method for the customFieldFeedback field
     * @return the current value of the variable customFieldFeedback
     */
    public List<String> getCustomFieldFeedback() {
        return customFieldFeedback;
    }

    /**
     * A setter method for customFieldFeedback
     * @param customFieldFeedback the value to be assigned
     */
    public void setCustomFieldFeedback(final List<String> customFieldFeedback) {
        this.customFieldFeedback = customFieldFeedback;
    }

    /**
     * A getter method for the customMethodFeedback field
     * @return the current value of the variable customMethodFeedback
     */
    public List<String> getCustomMethodFeedback() {
        return customMethodFeedback;
    }

    /**
     * A setter method for customMethodFeedback
     * @param customMethodFeedback the value to be assigned
     */
    public void setCustomMethodFeedback(final List<String> customMethodFeedback) {
        this.customMethodFeedback = customMethodFeedback;
    }

    /**
     * A getter method for the customClassFeedback field
     * @return the current value of the variable customClassFeedback
     */
    public List<String> getCustomClassFeedback() {
        return customClassFeedback;
    }

    /**
     * A setter method for customClassFeedback
     * @param customClassFeedback the value to be assigned
     */
    public void setCustomClassFeedback(final List<String> customClassFeedback) {
        this.customClassFeedback = customClassFeedback;
    }

    /**
     * A getter method for the customInheritanceFeedback field
     * @return the current value of the variable customInheritanceFeedback
     */
    public List<String> getCustomInheritanceFeedback() {
        return customInheritanceFeedback;
    }

    /**
     * A setter method for customInheritanceFeedback
     * @param customInheritanceFeedback the value to be assigned
     */
    public void setCustomInheritanceFeedback(final List<String> customInheritanceFeedback) {
        this.customInheritanceFeedback = customInheritanceFeedback;
    }

    /**
     * A getter method for the fullyQualifiedName field
     * @return the current value of the variable fullyQualifiedName
     */
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    /**
     * A setter method for fullyQualifiedName
     * @param fullyQualifiedName the value to be assigned
     */
    public void setFullyQualifiedName(final String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    /**
     * A getter method for the matchExactFieldAmount field
     * @return the current value of the variable matchExactFieldAmount
     */
    public boolean isMatchExactFieldAmount() {
        return matchExactFieldAmount;
    }

    /**
     * A setter method for matchExactFieldAmount
     * @param matchExactFieldAmount the value to be assigned
     */
    public void setMatchExactFieldAmount(final boolean matchExactFieldAmount) {
        this.matchExactFieldAmount = matchExactFieldAmount;
    }

    /**
     * A getter method for the matchExactMethodAmount field
     * @return the current value of the variable matchExactMethodAmount
     */
    public boolean isMatchExactMethodAmount() {
        return matchExactMethodAmount;
    }

    /**
     * A setter method for matchExactMethodAmount
     * @param matchExactMethodAmount the value to be assigned
     */
    public void setMatchExactMethodAmount(final boolean matchExactMethodAmount) {
        this.matchExactMethodAmount = matchExactMethodAmount;
    }
}
