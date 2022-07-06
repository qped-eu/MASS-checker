package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

public class ExpectedElement {

    private List<String> possibleAccessModifiers;
    private List<String> nonAccessModifiers;
    private String type;
    private String name;

    public ExpectedElement(List<String> accessMod, List<String> nonAccessMods, String type, String name) {
        this.possibleAccessModifiers = accessMod;
        this.nonAccessModifiers = nonAccessMods;
        this.type = type;
        this.name = name;
    }

    public List<String> getPossibleAccessModifiers() {
        return possibleAccessModifiers;
    }

    public void setPossibleAccessModifiers(List<String> possibleAccessModifiers) {
        this.possibleAccessModifiers = possibleAccessModifiers;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpectedElement that = (ExpectedElement) o;
        return Objects.equals(possibleAccessModifiers, that.possibleAccessModifiers) &&
                Objects.equals(nonAccessModifiers, that.nonAccessModifiers) &&
                Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(possibleAccessModifiers, nonAccessModifiers, type, name);
    }
}
