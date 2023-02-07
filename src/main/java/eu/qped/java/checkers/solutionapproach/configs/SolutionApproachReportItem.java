package eu.qped.java.checkers.solutionapproach.configs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SolutionApproachReportItem {

    private SemanticSettingItem relatedSemanticSettingItem;
    private String errorCode;

    private int solutionForCounter;
    private int solutionForEachCounter;
    private int solutionDoWhileCounter;
    private int solutionWhileCounter;
    private int solutionIfElseCounter;
    private boolean solutionHasLoop;
    private boolean solutionHasRecursiveMethodCall;
    private String solutionReturnType;

}
