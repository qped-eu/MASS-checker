package eu.qped.java.checkers.design.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Jannik Seus
 */
@Data
@AllArgsConstructor
public class DesignCheckEntry implements Comparable<DesignCheckEntry>{

    private String className;
    private List<DesignCheckMessage> metricsForClass;


    @Override
    public int compareTo(DesignCheckEntry o) {
        return this.getClassName().compareTo(o.getClassName());
    }
}
