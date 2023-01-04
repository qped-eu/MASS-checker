package eu.qped.java.checkers.metrics.data;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.AMC;
import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.CE;
import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.LCOM;
import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.WMC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;

/**
 * Test class for {@link ClassMetricsMessageSingle}.
 *
 * @author Jannik Seus
 */
public class ClassMetricsMessageSingleTest extends ClassMetricsMessageTest {


    protected ClassMetricsMessage classMetricsMessage1;
    protected ClassMetricsMessage classMetricsMessage2;
    protected ClassMetricsMessage classMetricsMessage3;
    protected ClassMetricsMessage classMetricsMessage4;

    @Override
    @BeforeEach
    void setUp() {
        classMetricsMessage1 = new ClassMetricsMessageSingle(AMC, 28.0);
        classMetricsMessage2 = new ClassMetricsMessageSingle(CE, 3.0);
        classMetricsMessage3 = new ClassMetricsMessageSingle(LCOM, 2.032);
        classMetricsMessage4 = new ClassMetricsMessageSingle(WMC, 6.503);
        sortedClassMetricsMessages = new ArrayList<>(List.of(classMetricsMessage1, classMetricsMessage2, classMetricsMessage3, classMetricsMessage4));
        randomClassMetricsMessages = new ArrayList<>(List.of(classMetricsMessage3, classMetricsMessage1, classMetricsMessage4, classMetricsMessage2));

    }

    @Test
    void getMetricValueTest() {

        double metricValue1 = ((ClassMetricsMessageSingle) classMetricsMessage1).getMetricValue();
        double metricValue2 = ((ClassMetricsMessageSingle) classMetricsMessage2).getMetricValue();
        double metricValue3 = ((ClassMetricsMessageSingle) classMetricsMessage3).getMetricValue();
        double metricValue4 = ((ClassMetricsMessageSingle) classMetricsMessage4).getMetricValue();

        assertEquals( 28.0, metricValue1);
        assertEquals( 3.0, metricValue2);
        assertEquals( 2.032, metricValue3);
        assertEquals( 6.503, metricValue4);
    }

    @Override
    protected ArrayList<ClassMetricsMessage> createRandomMetricsCheckerMessages() {
        return new ArrayList<>(List.of(classMetricsMessage3, classMetricsMessage1, classMetricsMessage4, classMetricsMessage2));
    }

    @Override
    protected ArrayList<ClassMetricsMessage> createSortedMetricsCheckerMessages() {
        return new ArrayList<>(List.of(classMetricsMessage1, classMetricsMessage2, classMetricsMessage3, classMetricsMessage4));
    }
}
