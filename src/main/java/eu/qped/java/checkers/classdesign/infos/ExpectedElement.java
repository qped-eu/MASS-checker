package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

public class ExpectedElement {

    private final boolean isExactMatch;
    private final List<String> possibleAccessModifiers;
    private final List<String> possibleNonAccessModifiers;
    private final List<String> types;
    private String name;

    private final boolean containsYes;

    public ExpectedElement(List<String> accessMod, List<String> nonAccessMods, List<String> types, String name,
                           boolean isExactMatch,
                           boolean containsYes) {
        this.isExactMatch = isExactMatch;
        this.possibleAccessModifiers = accessMod;
        this.possibleNonAccessModifiers = nonAccessMods;
        this.types = types;
        this.name = name;
        this.containsYes = containsYes;
    }

    public boolean isContainsYes() {
        return containsYes;
    }

    public boolean isExactMatch() {
        return isExactMatch;
    }

    public List<String> getPossibleAccessModifiers() {
        return possibleAccessModifiers;
    }

    public List<String> getPossibleNonAccessModifiers() {
        return possibleNonAccessModifiers;
    }

    public List<String> getTypes() {
        return types;
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
