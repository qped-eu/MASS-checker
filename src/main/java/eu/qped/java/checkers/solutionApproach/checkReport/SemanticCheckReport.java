package eu.qped.java.checkers.solutionApproach.checkReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SemanticCheckReport {

    private List<SemanticCheckReportEntry> entries;

}
