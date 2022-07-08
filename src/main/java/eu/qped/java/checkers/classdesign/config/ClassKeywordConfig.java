package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
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
    public String getType() {
        return getInterfaceType().equals(KeywordChoice.YES.toString()) ? ClassType.INTERFACE.toString() : ClassType.CLASS.toString();
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

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }
}
