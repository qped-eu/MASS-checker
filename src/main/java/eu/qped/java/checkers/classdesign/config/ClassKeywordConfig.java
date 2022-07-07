package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

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
