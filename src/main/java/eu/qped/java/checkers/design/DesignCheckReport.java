package eu.qped.java.checkers.design;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents a report for the {@link DesignChecker}.
 *
 * @author Jannik Seus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignCheckReport {
    /**
     * the map where the calculated metrics are stored in.
     * (k1: classname, v1: (k2: metric, v2: value) )
     */
    private Map<String, Map<Metric, Double>> metricsMap;
    private Map<Metric, Double> metricsThresholds;  //TODO implement thresholds, path, codeAsString
    private String codeAsString;
    private String path;

    public String toString() {
        StringBuilder reportString  = new StringBuilder("{ ");
        metricsMap.forEach((className, value) -> {
            reportString
                    .append("\n\t")
                    .append(className)
                    .append(": { ");

            value.forEach((key, value1) -> {
                String classMetric = key.toString();
                String metricValue = value1.toString();
                reportString
                        .append(classMetric)
                        .append(": ")
                        .append(metricValue)
                        .append(", ");
            });
            reportString
                    .deleteCharAt(reportString.lastIndexOf(","))
                    .append("}");
        });
        return reportString.append("\n}").toString();
    }
}
