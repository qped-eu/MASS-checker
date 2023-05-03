package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@Getter
@Setter

public class MethodKeywordConfig extends KeywordConfig  {

    private KeywordChoice abstractModifier;
    private KeywordChoice synchronizedModifier;
    private KeywordChoice nativeModifier;
    private KeywordChoice defaultModifier;
    private String type;

    public MethodKeywordConfig() {
        abstractModifier = KeywordChoice.DONTCARE;
        synchronizedModifier = KeywordChoice.DONTCARE;
        nativeModifier = KeywordChoice.DONTCARE;
        defaultModifier = KeywordChoice.DONTCARE;
        type = "";
    }

    @Override
    public Map<String, KeywordChoice> getNonAccessModifierMap() {
        Map<String, KeywordChoice> keywordChoiceMap = super.getNonAccessModifierMap();
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



}
