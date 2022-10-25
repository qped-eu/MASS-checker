package eu.qped.java.checkers.metrics.data.report;

import eu.qped.java.checkers.metrics.MetricsChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Class represents an entry used in {@link MetricsChecker} for storing
 * a classname and a list of {@link ClassMetricsMessage}s where this {@link #className}'s class metrics are stored.
 *
 * @author Jannik Seus
 */
@Getter
@AllArgsConstructor
public class ClassMetricsEntry implements Comparable<ClassMetricsEntry> {

    private String className;
    private List<ClassMetricsMessage> metricsForClass;

    /**
     * Compares DesignCheckEntry depending on alphabetically by {@link #className}.
     *
     * @param otherClassMetricsEntry DesignCheckEntry to which this DesignCheckEntry is compared to.
     */
    @Override
    public int compareTo(ClassMetricsEntry otherClassMetricsEntry) {
        return this.className.compareTo(otherClassMetricsEntry.getClassName());
    }
}
