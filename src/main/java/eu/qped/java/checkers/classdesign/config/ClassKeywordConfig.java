package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class ClassKeywordConfig extends KeywordConfig {

    private KeywordChoice abstractModifier;
    private KeywordChoice classType;
    private KeywordChoice interfaceType;

    public ClassKeywordConfig() {
        abstractModifier = KeywordChoice.DONTCARE;
        classType = KeywordChoice.DONTCARE;
        interfaceType = KeywordChoice.DONTCARE;
        setName("TestClass");
    }

    @Override
    public Map<String, KeywordChoice> getNonAccessModifierMap() {
        Map<String, KeywordChoice> keywordChoiceMap = super.getNonAccessModifierMap();
        keywordChoiceMap.put("abstract", getAbstractModifier());
        return keywordChoiceMap;
    }

    @Override
    public List<String> getPossibleTypes() {
        List<String> possibleTypes = new ArrayList<>();
        boolean containsYes = false;
        if(getInterfaceType().equals(KeywordChoice.YES)) {
            containsYes = true;
            possibleTypes.add(ClassType.INTERFACE.toString());
        }
        if(getClassType().equals(KeywordChoice.YES)) {
            containsYes = true;
            possibleTypes.add(ClassType.CLASS.toString());
        }

        if(!containsYes) {
            if(!getInterfaceType().equals(KeywordChoice.NO)) {
                possibleTypes.add(ClassType.INTERFACE.toString());
            }
            if(!getClassType().equals(KeywordChoice.NO)) {
                possibleTypes.add(ClassType.CLASS.toString());
            }
        }
        return possibleTypes;
    }


}
