package eu.qped.java.checkers.metrics.data.report;

import lombok.Getter;

import java.util.Map;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;

/**
 * Subclass for metric messages when multiple values are calculated for one class,
 * e.g. Cyclomatic Complexity for each method in a given class.
 *
 * @author Jannik Seus
 */
@Getter
public class ClassMetricsMessageMulti extends ClassMetricsMessage {

    private final Map<String, Integer> metricValues;

    /**
     * Main constructor.
     *
     * @param metric       the given metric
     * @param metricValues the calculated values of the given metric for a class
     */
    public ClassMetricsMessageMulti(Metric metric, Map<String, Integer> metricValues) {
        super(metric);
        this.metricValues = metricValues;
    }
}
