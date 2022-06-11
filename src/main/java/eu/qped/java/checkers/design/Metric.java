package eu.qped.java.checkers.design;

/**
 * Metrics enum representing all possible class metrics for the design checker.
 *
 * @author Jannik Seus
 */
public enum Metric {

    WMC("Weighted methods per class"),
    DIT("Depth of inheritance tree"),
    NOC("Number of Children"),
    CBO("Coupling between object classes"),
    RFC("Response for a Class"),
    LCOM("Lack of cohesion in methods"),
    CA("Afferent coupling"),
    CE("Efferent coupling"),
    NPM("Number of Public Methods for a class"),
    CIS("Class Interface Size"),    //TODO MISSING?
    LCOM3("Lack of cohesion in methods Henderson-Sellers version"),
    LOC("Lines of Code"),
    DAM("Data Access Metric"),
    MOA("Measure of Aggregation"),
    MFA("Measure of Functional Abstraction"),
    CAM("Cohesion Among Methods of Class"),
    IC("Inheritance Coupling"),
    CBM("Coupling Between Methods"),
    AMC("Average Method Complexity"),
    CC("McCabe's Cyclomatic Complexity"); // MISSING?

    Metric(String description) {}
}
