package eu.qped.java.checkers.coverage.enums;

/**
 * FeedbackType defines a feedback type:
 * <ul>
 *     <li>{@link #TEST} default feedback for failed test</li>
 *     <li>{@link #COVERAGE} default feedback for a not covered statement</li>
 *     <li>{@link #CUSTOM} custom feedback for failed test or not covered statements</li>
 * </ul>
 * @author Herfurth
 * @version 1.0
 */
public enum FeedbackType {
    COVERAGE,
    TEST,
    CUSTOM
}
