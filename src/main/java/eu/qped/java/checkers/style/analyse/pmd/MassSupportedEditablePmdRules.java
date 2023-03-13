package eu.qped.java.checkers.style.analyse.pmd;

/**

 This class represents a list of PMD rules related to code quality and style
 that are supported by the tool. These rules are specific to the mass (size and complexity)
 of a Java class, including excessive class length, too many fields, and high cyclomatic complexity.
 It also includes naming conventions for classes, methods, local variables, and method parameters.
 The rules are stored as public final static Strings.
 */
public final class MassSupportedEditablePmdRules {

    /**

     The name of the PMD rule that checks for excessive class length.
     */
    public final static String EXCESSIVE_CLASS_LENGTH = "ExcessiveClassLength";
    /**

     The name of the PMD rule that checks for adherence to class naming conventions.
     */
    public final static String CLASS_NAMING_CONVENTIONS = "ClassNamingConventions";
    /**

     The name of the PMD rule that checks for excessive method length.
     */
    public final static String EXCESSIVE_METHOD_LENGTH = "ExcessiveMethodLength";
    /**

     The name of the PMD rule that checks for adherence to method naming conventions.
     */
    public final static String METHOD_NAMING_CONVENTIONS = "MethodNamingConventions";
    /**

     The name of the PMD rule that checks for too many fields in a class.
     */
    public final static String TOO_MANY_FIELDS = "TooManyFields";
    /**

     The name of the PMD rule that checks for adherence to local variable naming conventions.
     */
    public final static String LOCAL_VARIABLE_NAMING_CONVENTIONS = "LocalVariableNamingConventions";
    /**

     The name of the PMD rule that checks for high cyclomatic complexity in methods.
     */
    public final static String CYCLOMATIC_COMPLEXITY = "CyclomaticComplexity";
    /**

     The name of the PMD rule that checks for adherence to method parameter naming conventions.
     */
    public final static String METHOD_PARAMETER_NAMING_CONVENTIONS = "MethodParameterNamingConventions";

}
