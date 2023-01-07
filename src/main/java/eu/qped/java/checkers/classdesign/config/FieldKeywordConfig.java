package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FieldKeywordConfig extends KeywordConfig {

    private KeywordChoice transientModifier;
    private KeywordChoice volatileModifier;
    private String type;

    public FieldKeywordConfig() {

        transientModifier = KeywordChoice.DONTCARE;
        volatileModifier = KeywordChoice.DONTCARE;
        type = "";
    }

    @Override
    public Map<String, KeywordChoice> getNonAccessModifierMap() {
        Map<String, KeywordChoice> keywordChoiceMap = super.getNonAccessModifierMap();
        keywordChoiceMap.put("transient", getTransientModifier());
        keywordChoiceMap.put("volatile", getVolatileModifier());
        return keywordChoiceMap;
    }

    @Override
    public List<String> getPossibleTypes() {
        return Collections.singletonList(type);
    }


}
