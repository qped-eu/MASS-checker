package eu.qped.java.checkers.metrics.data;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.CC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageMulti;

/**
 * Test class for {@link ClassMetricsMessageMulti}.
 *
 * @author Jannik Seus
 */
public class ClassMetricsMessageMultiTest extends ClassMetricsMessageTest {


    protected ClassMetricsMessage classMetricsMessage1;

    @Override
    @BeforeEach
    void setUp() {
        Map<String, Integer> metricValues = Map.of("void method2()", 2, "void method3()", 4);
        classMetricsMessage1 = new ClassMetricsMessageMulti(CC, metricValues);
        randomClassMetricsMessages = new ArrayList<>(List.of(classMetricsMessage1));
        sortedClassMetricsMessages = new ArrayList<>(List.of(classMetricsMessage1));
    }

    // compareTo does not make sense here (yet) because CC is the only possible metric type of
    // DesignCheckMessageMulti implemented.


    @Test
    void getMetricValuesTest() {
        String key1 = "void method2()";
        String key2 = "void method3()";

        Map<String, Integer> metricValues = ((ClassMetricsMessageMulti) classMetricsMessage1).getMetricValues();

        assertTrue(metricValues.containsKey(key1));
        assertTrue(metricValues.containsKey(key2));

        assertEquals(2, metricValues.get(key1));
        assertEquals(4, metricValues.get(key2));
    }

    @Override
    protected ArrayList<ClassMetricsMessage> createRandomMetricsCheckerMessages() {
        return new ArrayList<>(List.of(classMetricsMessage1));
    }

    @Override
    protected ArrayList<ClassMetricsMessage> createSortedMetricsCheckerMessages() {
        return new ArrayList<>(List.of(classMetricsMessage1));
    }
}
