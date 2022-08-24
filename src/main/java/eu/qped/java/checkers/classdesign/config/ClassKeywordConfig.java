package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassKeywordConfig extends KeywordConfig {

    private String abstractModifier;
    private String classType;
    private String interfaceType;

    public ClassKeywordConfig() {
        abstractModifier = KeywordChoice.DONTCARE.toString();
        classType = KeywordChoice.DONTCARE.toString();
        interfaceType = KeywordChoice.DONTCARE.toString();
        setName("TestClass");
    }

    @Override
    public Map<String, String> getNonAccessModifierMap() {
        Map<String, String> keywordChoiceMap = super.getNonAccessModifierMap();
        keywordChoiceMap.put("abstract", getAbstractModifier());
        return keywordChoiceMap;
    }

    @Override
    public List<String> getPossibleTypes() {
        List<String> possibleTypes = new ArrayList<>();
        boolean containsYes = false;
        if(getInterfaceType().equals(KeywordChoice.YES.toString())) {
            containsYes = true;
            possibleTypes.add(ClassType.INTERFACE.toString());
        }
        if(getClassType().equals(KeywordChoice.YES.toString())) {
            containsYes = true;
            possibleTypes.add(ClassType.CLASS.toString());
        }

        if(!containsYes) {
            if(!getInterfaceType().equals(KeywordChoice.NO.toString())) {
                possibleTypes.add(ClassType.INTERFACE.toString());
            }
            if(!getClassType().equals(KeywordChoice.NO.toString())) {
                possibleTypes.add(ClassType.CLASS.toString());
            }
        }
        return possibleTypes;
    }

    public String getAbstractModifier() {
        return abstractModifier;
    }

    public void setAbstractModifier(String abstractModifier) {
        this.abstractModifier = abstractModifier;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String choice) {
        this.classType = choice;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }
}
