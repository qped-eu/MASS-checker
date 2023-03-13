package eu.qped.java.checkers.style.analyse.pmd;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 A custom exception class that represents a configuration error related to PMD rules.
 This exception can be thrown when a rule or property is not found in a PMD configuration file.
 */
@Data
@NoArgsConstructor
@Builder
public class PmdConfigException extends Exception {

    /**
     * The name of the PMD rule that caused the exception.
     */
    private String ruleName;
    /**
     * The name of the PMD rule property that caused the exception.
     */
    private String propName;

    /**
     * Constructs a new PmdConfigException with a default error message based on the rule name and property name.
     * @param ruleName The name of the PMD rule that caused the exception.
     * @param propName The name of the PMD rule property that caused the exception.
     */
    public PmdConfigException(String ruleName, String propName) {
        super((ruleName == null ? "ruleName must not be null"
                : (propName == null ? "propertyName must not be null" : "Element not found"))
                + " (ruleName: " + ruleName + ", propName: " + propName + ")");
        this.ruleName = ruleName;
        this.propName = propName;

    }

    /**
     * Constructs a new PmdConfigException with a custom error message based on the rule name, property name, and message.
     * @param message The custom error message.
     * @param ruleName The name of the PMD rule that caused the exception.
     * @param propName The name of the PMD rule property that caused the exception.
     */
    public PmdConfigException(String message, String ruleName, String propName) {
        super(message + " (ruleName: " + ruleName + ", propName: " + propName + ")");
        this.ruleName = ruleName;
        this.propName = propName;
    }
}
