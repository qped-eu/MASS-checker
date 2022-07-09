package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KeywordConfig {

    private String allowExactModifierMatching;

    private String publicModifier;
    private String protectedModifier;
    private String privateModifier;
    private String packagePrivateModifier;

    private String emptyNonAccessModifier;
    private String staticModifier;
    private String finalModifier;

    private String name;

    public KeywordConfig() {
        allowExactModifierMatching = "false";
        publicModifier = KeywordChoice.DONTCARE.toString();
        protectedModifier = KeywordChoice.DONTCARE.toString();
        privateModifier = KeywordChoice.DONTCARE.toString();
        packagePrivateModifier = KeywordChoice.DONTCARE.toString();

        emptyNonAccessModifier = KeywordChoice.DONTCARE.toString();
        staticModifier = KeywordChoice.DONTCARE.toString();
        finalModifier = KeywordChoice.DONTCARE.toString();

        name = "";
    }

    public Map<String, String> getAccessModifierMap() {
        Map<String, String> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("public", getPublicModifier());
        keywordChoiceMap.put("protected", getProtectedModifier());
        keywordChoiceMap.put("private", getPrivateModifier());
        keywordChoiceMap.put("", getPackagePrivateModifier());
        return keywordChoiceMap;
    }

    public Map<String, String> getNonAccessModifierMap() {
        Map<String, String> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("static", getStaticModifier());
        keywordChoiceMap.put("final", getFinalModifier());
        keywordChoiceMap.put("", getEmptyNonAccessModifier());
        return keywordChoiceMap;
    }

    public abstract List<String> getPossibleTypes();

    public String getAllowExactModifierMatching() {
        return allowExactModifierMatching;
    }

    public void setAllowExactModifierMatching(String allowExactModifierMatching) {
        this.allowExactModifierMatching = allowExactModifierMatching;
    }

    public String getPublicModifier() {
        return publicModifier;
    }

    public void setPublicModifier(String publicModifier) {
        this.publicModifier = publicModifier;
    }

    public String getProtectedModifier() {
        return protectedModifier;
    }

    public void setProtectedModifier(String protectedModifier) {
        this.protectedModifier = protectedModifier;
    }

    public String getPrivateModifier() {
        return privateModifier;
    }

    public void setPrivateModifier(String privateModifier) {
        this.privateModifier = privateModifier;
    }

    public String getPackagePrivateModifier() {
        return packagePrivateModifier;
    }

    public void setPackagePrivateModifier(String packagePrivateModifier) {
        this.packagePrivateModifier = packagePrivateModifier;
    }

    public String getEmptyNonAccessModifier() {
        return emptyNonAccessModifier;
    }

    public void setEmptyNonAccessModifier(String emptyNonAccessModifier) {
        this.emptyNonAccessModifier = emptyNonAccessModifier;
    }



    public String getStaticModifier() {
        return staticModifier;
    }

    public void setStaticModifier(String staticModifier) {
        this.staticModifier = staticModifier;
    }

    public String getFinalModifier() {
        return finalModifier;
    }

    public void setFinalModifier(String finalModifier) {
        this.finalModifier = finalModifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
