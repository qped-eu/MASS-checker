package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

import java.util.Map;

public class FieldKeywordConfig extends KeywordConfig {

    private String transientModifier;
    private String volatileModifier;
    private String type;

    public FieldKeywordConfig() {

        transientModifier = KeywordChoice.DONTCARE.toString();
        volatileModifier = KeywordChoice.DONTCARE.toString();
        type = "";
    }

    @Override
    public Map<String, String> getNonAccessModifierMap() {
        Map<String, String> keywordChoiceMap = super.getNonAccessModifierMap();
        keywordChoiceMap.put("transient", getTransientModifier());
        keywordChoiceMap.put("volatile", getVolatileModifier());
        return keywordChoiceMap;
    }

    @Override
    public String getType() {
        return type;
    }

    public String getTransientModifier() {
        return transientModifier;
    }

    public void setTransientModifier(String transientModifier) {
        this.transientModifier = transientModifier;
    }

    public String getVolatileModifier() {
        return volatileModifier;
    }

    public void setVolatileModifier(String volatileModifier) {
        this.volatileModifier = volatileModifier;
    }

    public void setType(String type) {
        this.type = type;
    }
}
