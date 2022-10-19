package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

import java.util.ArrayList;
import java.util.List;

public class InheritsFromConfig extends KeywordConfig {

    private String classType;
    private String interfaceType;

    public InheritsFromConfig() {
        classType = KeywordChoice.DONTCARE.toString();;
        interfaceType = KeywordChoice.DONTCARE.toString();
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
