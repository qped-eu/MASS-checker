package eu.qped.java.checkers.metrics.data.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;

/**
 * Abstract class for metric messages.
 * For concrete Implementations see:
 * - {@link ClassMetricsMessageSingle} and
 * - {@link ClassMetricsMessageMulti} and
 *
 * @author Jannik Seus
 */
@Getter
@AllArgsConstructor
public abstract class ClassMetricsMessage implements Comparable<ClassMetricsMessage>{

    private Metric metric;

    @Override
    public int compareTo(ClassMetricsMessage otherClassMetricsMessage) {
        return this.metric.toString().compareTo(otherClassMetricsMessage.getMetric().toString());
    }
}
