package eu.qped.java.checkers.coverage.enums;

/**
 * ModifierType defines a modifier from a method.
 * <ul>
 *     <li>{@link #PRIVATE}  a method has the modifier private</li>
 *     <li>{@link #PROTECTED} a method has the modifier protected</li>
 *     <li>{@link #GET} a method name starts with the prefix get and the suffix of field name or only the field name</li>
 *     <li>{@link #SET} a method name starts with the prefix set and the suffix of field name</li>
 * </ul>
 * @author Herfurth
 * @version 1.0
 */
public enum ModifierType {
    PRIVATE,
    PROTECTED,
    GET,
    SET
}
