package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

public class ExpectedElement {

    private boolean isExactMatch;
    private List<String> possibleAccessModifiers;
    private List<String> possibleNonAccessModifiers;
    private List<String> types;
    private String name;

    public ExpectedElement(List<String> accessMod, List<String> nonAccessMods, List<String> types, String name, boolean isExactMatch) {
        this.isExactMatch = isExactMatch;
        this.possibleAccessModifiers = accessMod;
        this.possibleNonAccessModifiers = nonAccessMods;
        this.types = types;
        this.name = name;
    }

    public boolean isExactMatch() {
        return isExactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        isExactMatch = exactMatch;
    }

    public List<String> getPossibleAccessModifiers() {
        return possibleAccessModifiers;
    }

    public void setPossibleAccessModifiers(List<String> possibleAccessModifiers) {
        this.possibleAccessModifiers = possibleAccessModifiers;
    }

    public List<String> getPossibleNonAccessModifiers() {
        return possibleNonAccessModifiers;
    }

    public void setPossibleNonAccessModifiers(List<String> possibleNonAccessModifiers) {
        this.possibleNonAccessModifiers = possibleNonAccessModifiers;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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
                Objects.equals(possibleNonAccessModifiers, that.possibleNonAccessModifiers) &&
                Objects.equals(types, that.types) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(possibleAccessModifiers, possibleNonAccessModifiers, types, name);
    }
}
