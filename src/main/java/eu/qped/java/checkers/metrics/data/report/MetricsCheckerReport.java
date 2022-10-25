package eu.qped.java.checkers.metrics.data.report;

import eu.qped.java.checkers.metrics.MetricsChecker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a report for the {@link MetricsChecker}.
 *
 * @author Jannik Seus
 */

@AllArgsConstructor
@Builder
@Getter
@Setter
public class MetricsCheckerReport {

    private List<String> pathsToClassFiles;
    private List<ClassMetricsEntry> metricsMap;

}
