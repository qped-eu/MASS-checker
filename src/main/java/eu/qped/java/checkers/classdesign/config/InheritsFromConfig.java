package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class InheritsFromConfig extends KeywordConfig {

    private String classType;
    private String interfaceType;

    public InheritsFromConfig() {
        classType = KeywordChoice.DONTCARE.toString();;
        interfaceType = KeywordChoice.DONTCARE.toString();
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
