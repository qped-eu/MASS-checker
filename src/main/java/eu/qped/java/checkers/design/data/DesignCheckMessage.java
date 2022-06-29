package eu.qped.java.checkers.design.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;

/**
 * @author Jannik Seus
 */
@Data
@AllArgsConstructor
public class DesignCheckMessage implements Comparable<DesignCheckMessage>{

    private Metric metric;
    private double metricValue;

    @Override
    public int compareTo(DesignCheckMessage o) {
        return this.getMetric().toString().compareTo(o.getMetric().toString());
    }
}
