package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class ClassKeywordConfig extends KeywordConfig {

    private String classType;
    private String interfaceType;

    public ClassKeywordConfig() {
        classType = KeywordChoice.YES.toString();
        interfaceType = KeywordChoice.IGNORE.toString();
        setName("TestClass");
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
