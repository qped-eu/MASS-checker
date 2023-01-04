package eu.qped.java.checkers.metrics.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageMulti;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;

/**
 * Test class for {@link ClassMetricsMessage}.
 *
 * @author Jannik Seus
 */
abstract class ClassMetricsMessageTest {

    ArrayList<ClassMetricsMessage> randomClassMetricsMessages;
    ArrayList<ClassMetricsMessage> sortedClassMetricsMessages;

    @BeforeEach
    void setUp() {
        randomClassMetricsMessages = createRandomMetricsCheckerMessages();
        sortedClassMetricsMessages = createSortedMetricsCheckerMessages();
    }

    protected abstract ArrayList<ClassMetricsMessage> createRandomMetricsCheckerMessages();

    protected abstract ArrayList<ClassMetricsMessage> createSortedMetricsCheckerMessages();

    @Test
    void compareTo() {
        randomClassMetricsMessages.sort(Comparator.naturalOrder());
        assertArrayEquals(sortedClassMetricsMessages.toArray(), randomClassMetricsMessages.toArray());
    }

    @Test
    void getMetric() {
        ArrayList<ClassMetricsMessage> sortedClassMetricsMessages = createSortedMetricsCheckerMessages();
        if (sortedClassMetricsMessages.get(0) instanceof ClassMetricsMessageSingle) {
            Assertions.assertEquals(MetricCheckerEntryHandler.Metric.AMC, sortedClassMetricsMessages.get(0).getMetric());
            Assertions.assertEquals(MetricCheckerEntryHandler.Metric.CE, sortedClassMetricsMessages.get(1).getMetric());
            Assertions.assertEquals(MetricCheckerEntryHandler.Metric.LCOM, sortedClassMetricsMessages.get(2).getMetric());
            Assertions.assertEquals(MetricCheckerEntryHandler.Metric.WMC, sortedClassMetricsMessages.get(3).getMetric());
        } else if (sortedClassMetricsMessages.get(0) instanceof ClassMetricsMessageMulti) {
            Assertions.assertEquals(MetricCheckerEntryHandler.Metric.CC, sortedClassMetricsMessages.get(0).getMetric());
        }
    }
}