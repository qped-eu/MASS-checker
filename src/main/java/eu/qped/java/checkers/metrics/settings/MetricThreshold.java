package eu.qped.java.checkers.metrics.settings;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a class to define a {@link #lowerBound} and an {@link #upperBound} to a specific {@link #metric}.
 * The boundaries are meant to be inclusive.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
public class MetricThreshold implements Comparable<MetricThreshold> {

    private Metric metric;
    private double lowerBound;
    private double upperBound;
    private boolean noMax;

    /**
     * Constructor that sets a lower or upper threshold to a specific value.
     *
     * @param metric the given metric
     * @param upperBound the value of the threshold
     * @param lowerBound determines whether bound represents the lower or upper threshold
     * @param noMax determines whether this specific metric does not need an upper threshold
     */
    public MetricThreshold(Metric metric, double lowerBound, double upperBound, boolean noMax) {
        this.metric = metric;
        if (this.metric == null) {
            throw new IllegalStateException("A metric must be specified.");
        }
//        if (lowerBound == -1d || upperBound == -1d) {
//            throw new IllegalStateException("Illegal bound specified");
//        }

        if (lowerBound <= upperBound) {
            this.lowerBound = lowerBound;
            if (noMax && upperBound < 0) {
                this.upperBound = Double.MAX_VALUE;
            } else {
                this.upperBound = upperBound;
            }
        } else {
            this.lowerBound = upperBound;
            if (noMax && (lowerBound < 0)) {
                this.upperBound = Double.MAX_VALUE;
            } else {
                this.upperBound = lowerBound;
            }
        }
    }

    @Override
    public int compareTo(MetricThreshold o) {
        if (o == null) return 1;
        return this.metric.toString().compareTo(o.getMetric().toString());
    }
}
