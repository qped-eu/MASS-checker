package eu.qped.java.checkers.metrics.settings;

import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MetricConfig {

    private MetricThreshold metricThreshold;
    private MetricsFeedbackSuggestion metricsFeedbackSuggestion;

}