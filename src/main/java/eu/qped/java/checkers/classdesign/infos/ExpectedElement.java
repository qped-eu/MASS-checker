package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

/**
 The ExpectedElement class represents the expected code for a class, method, or field. It includes information such as the expected access modifiers, non-access modifiers, variable types, name, and whether or not the expected code is an exact match or contains the actual code.
 */
public class ExpectedElement {

    private final boolean isExactMatch;
    private final List<String> possibleAccessModifiers;
    private final List<String> possibleNonAccessModifiers;
    private final List<String> types;
    private String name;

    private final boolean containsYes;

    /**
     Class constructor
     @param accessMod The expected access modifiers for the class, method, or field.
     @param nonAccessMods The expected non-access modifiers for the class, method, or field.
     @param types The expected variable types for the class, method, or field.
     @param name The expected name for the class, method, or field.
     @param isExactMatch A boolean expressing if the code in an exact match.
     @param containsYes A boolean representing if the expected code contains the actual code.
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
     A getter method for the containsYes field
     @return the current value of the variable containsYes
     */
    public boolean isContainsYes() {
        return containsYes;
    }
    /**
     A getter method for the isExactMatch field
     @return the current value of the variable isExactMatch
     */
    public boolean isExactMatch() {
        return isExactMatch;
    }

    /**
     A getter method for the possibleAccessModifiers field
     @return the current value of the variable possibleAccessModifiers
     */
    public List<String> getPossibleAccessModifiers() {
        return possibleAccessModifiers;
    }

    /**
     A getter method for the possibleNonAccessModifiers field
     @return the current value of the variable possibleNonAccessModifiers
     */
    public List<String> getPossibleNonAccessModifiers() {
        return possibleNonAccessModifiers;
    }

    /**
     A getter method for the types field
     @return the current value of the variable types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     A getter method for the types name
     @return the current value of the variable name
     */
    public String getName() {
        return name;
    }

    /**
     A setter method for the name field.
     @param name the value to be assigned to the name field.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * A method to compare two objects of the class ExpectedElement.
     * It overrides the default implementation of the {@link Object#equals(Object)} method.
     * The comparison is based on the equality of all fields of the class, i.e. possibleAccessModifiers,
     * possibleNonAccessModifiers, types and name.
     * @param o The object to compare with.
     * @return true if the objects are equal, false if not.
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
     A method that generates a hash code for the ExpectedElement object.
     The hash code is generated based on the values of the possibleAccessModifiers, possibleNonAccessModifiers, types and name fields.
     @return an integer representing the hash code for the ExpectedElement object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(possibleAccessModifiers, possibleNonAccessModifiers, types, name);
    }
}
