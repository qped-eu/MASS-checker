package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

/**
 * A class representing the expected code
 */
public class ExpectedElement {

    private final boolean isExactMatch;
    private final List<String> possibleAccessModifiers;
    private final List<String> possibleNonAccessModifiers;
    private final List<String> types;
    private String name;

    private final boolean containsYes;

    /**
     * Class constructor
     * @param accessMod access modifiers
     * @param nonAccessMods non access modifiers
     * @param types variable types
     * @param name name
     * @param isExactMatch a boolean expressing if the code in an exact match
     * @param containsYes a boolean representing if the expected code contaions the actual code
     */
    public ExpectedElement(final List<String> accessMod, final List<String> nonAccessMods, final List<String> types, final String name,
                           final boolean isExactMatch,
                           final boolean containsYes) {
        this.isExactMatch = isExactMatch;
        this.possibleAccessModifiers = accessMod;
        this.possibleNonAccessModifiers = nonAccessMods;
        this.types = types;
        this.name = name;
        this.containsYes = containsYes;
    }

    /**
     * A getter method for the containsYes field
     * @return the current value of the variable containsYes
     */
    public boolean isContainsYes() {
        return containsYes;
    }

    /**
     * A getter method for the isExactMatch field
     * @return the current value of the variable isExactMatch
     */
    public boolean isExactMatch() {
        return isExactMatch;
    }

    /**
     * A getter method for the possibleAccessModifiers field
     * @return the current value of the variable possibleAccessModifiers
     */
    public List<String> getPossibleAccessModifiers() {
        return possibleAccessModifiers;
    }

    /**
     * A getter method for the possibleNonAccessModifiers field
     * @return the current value of the variable possibleNonAccessModifiers
     */
    public List<String> getPossibleNonAccessModifiers() {
        return possibleNonAccessModifiers;
    }

    /**
     * A getter method for the types field
     * @return the current value of the variable types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * A getter method for the types name
     * @return the current value of the variable name
     */
    public String getName() {
        return name;
    }

    /**
     * A setter method for name
     * @param name the value to be assigned
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * A methode to compare 2 obects
     * @param o the object to compare to
     * @return true of the objects are equal, false if not
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ExpectedElement that = (ExpectedElement) o;
        return Objects.equals(possibleAccessModifiers, that.possibleAccessModifiers) &&
                Objects.equals(possibleNonAccessModifiers, that.possibleNonAccessModifiers) &&
                Objects.equals(types, that.types) && Objects.equals(name, that.name);
    }

    /**
     * A methof to create the hash code
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(possibleAccessModifiers, possibleNonAccessModifiers, types, name);
    }
}
