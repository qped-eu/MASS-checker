package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public abstract class KeywordConfig {

    private boolean allowExactModifierMatching;

    private KeywordChoice publicModifier;
    private KeywordChoice protectedModifier;
    private KeywordChoice privateModifier;
    private KeywordChoice packagePrivateModifier;

    private KeywordChoice emptyNonAccessModifier;
    private KeywordChoice staticModifier;
    private KeywordChoice finalModifier;

    private String name;

    public KeywordConfig() {
        allowExactModifierMatching = false;
        publicModifier = KeywordChoice.DONTCARE;
        protectedModifier = KeywordChoice.DONTCARE;
        privateModifier = KeywordChoice.DONTCARE;
        packagePrivateModifier = KeywordChoice.DONTCARE;

        emptyNonAccessModifier = KeywordChoice.DONTCARE;
        staticModifier = KeywordChoice.DONTCARE;
        finalModifier = KeywordChoice.DONTCARE;

        name = "";
    }

    public Map<String, KeywordChoice> getAccessModifierMap() {
        Map<String, KeywordChoice> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("public", getPublicModifier());
        keywordChoiceMap.put("protected", getProtectedModifier());
        keywordChoiceMap.put("private", getPrivateModifier());
        keywordChoiceMap.put("", getPackagePrivateModifier());
        return keywordChoiceMap;
    }

    public Map<String, KeywordChoice> getNonAccessModifierMap() {
        Map<String, KeywordChoice> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("static", getStaticModifier());
        keywordChoiceMap.put("final", getFinalModifier());
        keywordChoiceMap.put("", getEmptyNonAccessModifier());
        return keywordChoiceMap;
    }

    public abstract List<String> getPossibleTypes();

    public boolean isAllowExactModifierMatching() {
        return allowExactModifierMatching;
    }

    }



