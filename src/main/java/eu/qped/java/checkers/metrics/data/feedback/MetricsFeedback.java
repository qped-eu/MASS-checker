package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.framework.Feedback;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;

/**
 * Data class representing the feedback for design presented to the user.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
public class MetricsFeedback extends Feedback {

    private String className;
    private Metric metric;
    private Double value;
    private String suggestion;

    @Builder
    public MetricsFeedback(String className, String body, Metric metric, Double value, String suggestion) {
        super(body);
        this.className = className;
        this.metric = metric;
        this.value = value;
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "In class '" + getClassName() + ".java'" + "\n" +
                getMetric() + " (" + super.getBody() + ")" + "\n" +
                "Measured at: " + getValue() + "\n" +
                suggestion;
    }
}
