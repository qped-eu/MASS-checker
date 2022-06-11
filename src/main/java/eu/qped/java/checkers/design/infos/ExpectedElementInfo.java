package eu.qped.java.checkers.design.infos;

import java.util.List;

public class ExpectedElementInfo {

    private String accessModifier;
    private List<String> nonAccessModifiers;
    private String type;
    private String name;


    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public List<String> getNonAccessModifiers() {
        return nonAccessModifiers;
    }

    public void setNonAccessModifiers(List<String> nonAccessModifiers) {
        this.nonAccessModifiers = nonAccessModifiers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
