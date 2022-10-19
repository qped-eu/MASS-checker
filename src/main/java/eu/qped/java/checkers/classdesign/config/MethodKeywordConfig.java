package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MethodKeywordConfig extends KeywordConfig  {

    private String abstractModifier;
    private String synchronizedModifier;
    private String nativeModifier;
    private String defaultModifier;
    private String type;

    public MethodKeywordConfig() {
        abstractModifier = KeywordChoice.DONTCARE.toString();
        synchronizedModifier = KeywordChoice.DONTCARE.toString();
        nativeModifier = KeywordChoice.DONTCARE.toString();
        defaultModifier = KeywordChoice.DONTCARE.toString();
        type = "";
    }

    @Override
    public Map<String, String> getNonAccessModifierMap() {
        Map<String, String> keywordChoiceMap = super.getNonAccessModifierMap();
        keywordChoiceMap.put("abstract", getAbstractModifier());
        keywordChoiceMap.put("synchronized", getSynchronizedModifier());
        keywordChoiceMap.put("native", getNativeModifier());
        keywordChoiceMap.put("default", getDefaultModifier());
        return keywordChoiceMap;
    }
    @Override
    public List<String> getPossibleTypes() {
        return Collections.singletonList(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbstractModifier() {
        return abstractModifier;
    }

    public void setAbstractModifier(String abstractModifier) {
        this.abstractModifier = abstractModifier;
    }

    public String getDefaultModifier() {
        return defaultModifier;
    }

    public void setDefaultModifier(String defaultModifier) {
        this.defaultModifier = defaultModifier;
    }

    public String getSynchronizedModifier() {
        return synchronizedModifier;
    }

    public void setSynchronizedModifier(String synchronizedModifier) {
        this.synchronizedModifier = synchronizedModifier;
    }

    public String getNativeModifier() {
        return nativeModifier;
    }

    public void setNativeModifier(String nativeModifier) {
        this.nativeModifier = nativeModifier;
    }



}
