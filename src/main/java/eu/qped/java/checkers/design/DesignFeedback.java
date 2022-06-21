package eu.qped.java.checkers.design;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler;
import eu.qped.java.checkers.design.configuration.DesignSettings;
import eu.qped.java.checkers.design.configuration.MetricThreshold;
import eu.qped.java.checkers.design.data.DesignCheckEntry;
import eu.qped.java.checkers.design.data.DesignCheckMessage;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;

/**
 * Data class representing the feedback for design presented to the user.
 *
 * @author Jannik Seus
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class DesignFeedback extends Feedback {
    private String className;
    private Metric metric;
    private Double value;
    private boolean lowerThresholdReached;
    private boolean upperThresholdReached;
    private String suggestion;
    //private String example; //TODO schwierig, sich hier bei Klassendesign kurz zu halten

    @Builder
    public DesignFeedback(String className, String body, Metric metric, Double value, boolean lowerThresholdReached, boolean upperThresholdReached, String suggestion) {
        super(body);
        this.className = className;
        this.metric = metric;
        this.value = value;
        this.lowerThresholdReached = lowerThresholdReached;
        this.upperThresholdReached = upperThresholdReached;
        this.suggestion = suggestion;
    }

    /**
     * Generates a suggestion for the student depending on the exceeding of a metric's already calculated value.
     *
     * @param metric the given metric
     * @param lowerThreshold the metric's lower threshold not to be exceeded
     * @param upperThreshold the metric's upper threshold not to be exceeded
     * @return a nicely formatted suggestion as String.
     */
    public static String generateSuggestion(Metric metric, boolean lowerThreshold, boolean upperThreshold) {
        if (!lowerThreshold && !upperThreshold) {
            return "You are within the " + metric.toString() + "'s threshold.";
        } else if (lowerThreshold) {
            return "The " + metric.toString() + "'s value is too low.";
        } else if (upperThreshold) {
            return "The " + metric.toString() + "'s value is too high.";
        } else {
            throw new IllegalArgumentException();
        }
    }
    //TODO methods: generateMetricSuggestionLowerThExceeded(); and generateMetricSuggeDesstionUpperThExceeded

    /**
     * Generates the DesignFeedback to the corresponding classes, metrics, and designSettings (min/max thresholds)
     *
     * @param metricsMap the map containing classnames, metrics and corresponding values
     * @param designSettings the settings on which the feedback depends on //TODO wip
     * @return the generated Feedback as a List.
     */
    public static List<DesignFeedback> generateDesignFeedback(List<DesignCheckEntry> metricsMap, DesignSettings designSettings) {
        List<DesignFeedback> feedbacks = new ArrayList<>();

        metricsMap.forEach(designCheckEntry -> {

            String className = designCheckEntry.getClassName();
            List<DesignCheckMessage> metricsForClass = designCheckEntry.getMetricsForClass();

            if (metricsForClass != null) {
                metricsForClass.forEach(metricForClass -> {
                    Metric metric = metricForClass.getMetric();
                    double metricValue = metricForClass.getMetricValue();

                    boolean lowerThreshold = isThresholdReached(metric, designSettings, metricValue, true);
                    boolean upperThreshold = isThresholdReached(metric, designSettings, metricValue, false);
                    String suggestion = generateSuggestion(metric, lowerThreshold, upperThreshold);

                    feedbacks.add(new DesignFeedback(className, metric.getDescription(), metric, metricValue, lowerThreshold, upperThreshold, suggestion));
                });
            }
        });
        return feedbacks;
    }

    /**
     * Checks whether the lower or upper threshold of a given metricThreshold was exceeded.
     *
     * @param metric the given metric
     * @param designSettings the settings for design guidelines
     * @param value the given metricThreshold's value
     * @param lower determines whether to check the lower or upper threshold
     * @return whether the min(lower=true) or max(lower=false) threshold was exceeded.
     */
    private static boolean isThresholdReached(Metric metric, DesignSettings designSettings, double value, boolean lower) {

        switch (metric) {
            case AMC:
                return lower ? value < designSettings.getAmc().getMinThreshold() : value > designSettings.getAmc().getMaxThreshold();
            case CAM:
                return lower ? value < designSettings.getCam().getMinThreshold() : value > designSettings.getCam().getMaxThreshold();
            case CA:
                return lower ? value < designSettings.getCa().getMinThreshold() : value > designSettings.getCa().getMaxThreshold();
            case CBM:
                return lower ? value < designSettings.getCbm().getMinThreshold() : value > designSettings.getCbm().getMaxThreshold();
            case CBO:
                return lower ? value < designSettings.getCbo().getMinThreshold() : value > designSettings.getCbo().getMaxThreshold();
            case CC:
                return lower ? value < designSettings.getCc().getMinThreshold() : value > designSettings.getCc().getMaxThreshold();
            case CE:
                return lower ? value < designSettings.getCe().getMinThreshold() : value > designSettings.getCe().getMaxThreshold();
            case CIS:
                return lower ? value < designSettings.getCis().getMinThreshold() : value > designSettings.getCis().getMaxThreshold();
            case DAM:
                return lower ? value < designSettings.getDam().getMinThreshold() : value > designSettings.getDam().getMaxThreshold();
            case DIT:
                return lower ? value < designSettings.getDit().getMinThreshold() : value > designSettings.getDit().getMaxThreshold();
            case IC:
                return lower ? value < designSettings.getIc().getMinThreshold() : value > designSettings.getIc().getMaxThreshold();
            case LCOM:
                return lower ? value < designSettings.getLcom().getMinThreshold() : value > designSettings.getLcom().getMaxThreshold();
            case LCOM3:
                return lower ? value < designSettings.getLcom3().getMinThreshold() : value > designSettings.getLcom3().getMaxThreshold();
            case LOC:
                return lower ? value < designSettings.getLoc().getMinThreshold() : value > designSettings.getLoc().getMaxThreshold();
            case MOA:
                return lower ? value < designSettings.getMoa().getMinThreshold() : value > designSettings.getMoa().getMaxThreshold();
            case MFA:
                return lower ? value < designSettings.getMfa().getMinThreshold() : value > designSettings.getMfa().getMaxThreshold();
            case NOC:
                return lower ? value < designSettings.getNoc().getMinThreshold() : value > designSettings.getNoc().getMaxThreshold();
            case NPM:
                return lower ? value < designSettings.getNpm().getMinThreshold() : value > designSettings.getNpm().getMaxThreshold();
            case RFC:
                return lower ? value < designSettings.getRfc().getMinThreshold() : value > designSettings.getRfc().getMaxThreshold();
            case WMC:
                return lower ? value < designSettings.getWmc().getMinThreshold() : value > designSettings.getWmc().getMaxThreshold();
            default:
                throw new IllegalArgumentException("Illegal Metric given.");
        }
    }

    @Override
    public String toString() {
        StringBuilder feedbackString = new StringBuilder();
        if (lowerThresholdReached || upperThresholdReached) {
            if (lowerThresholdReached) {
                feedbackString.append("Lower ");
            } else {
                feedbackString.append("Upper ");
            }
            feedbackString
                    .append("threshold of metric '")
                    .append(this.metric.toString())
                    .append("' in class '").append(this.className)
                    .append("' exceeded.\t")
                    .append("Thresholds: ").append("(")
                    .append(this.metric.getDefaultThresholdMin())
                    .append(", ")
                    .append(this.metric.getDefaultThresholdMax())
                    .append(")");
        } else {
            feedbackString
                    .append("threshold of Metric '")
                    .append(this.metric.toString())
                    .append("' in class '").append(this.className)
                    .append("' not exceeded\t");
        }
        feedbackString
                .append("Value=").append(this.value)
                .append(",\t suggestion: ").append(this.metric.getDescription());
        return feedbackString.toString();

    }
}
