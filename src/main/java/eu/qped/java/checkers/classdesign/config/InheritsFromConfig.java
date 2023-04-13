package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class InheritsFromConfig extends KeywordConfig {

    private KeywordChoice classType;
    private KeywordChoice interfaceType;

    public InheritsFromConfig() {
        classType = KeywordChoice.DONTCARE;;
        interfaceType = KeywordChoice.DONTCARE;
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
