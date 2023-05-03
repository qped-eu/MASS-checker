package eu.qped.java.checkers.metrics.ckjm;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;
import gr.spinellis.ckjm.ClassMetrics;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class MetricCheckerEntryHandlerTest {
    private MetricCheckerEntryHandler handler;

    @Before
    public void setUp() {
        handler = new MetricCheckerEntryHandler();
    }

    @Test
    public void testHandleClass() {
        ClassMetrics metrics = new ClassMetrics();
        metrics.setAmc(1);
        metrics.setCam(2);
        metrics.setCbm(3);
        metrics.setDam(4);
        metrics.setDit(5);
        metrics.setLcom(6);
        metrics.setRfc(7);

        handler.handleClass("TestClass", metrics);

        List<ClassMetricsEntry> entries = handler.getOutputMetrics();
        assertEquals(1, entries.size());
        ClassMetricsEntry entry = entries.get(0);
        assertEquals("TestClass", entry.getClassName());

        ClassMetricsMessageSingle AMC = (ClassMetricsMessageSingle) entry.getMetrics().get(0);
        System.out.println(AMC.toString());
        assertEquals(1, AMC.getMetricValue(), 0);

        ClassMetricsMessageSingle Cam = (ClassMetricsMessageSingle) entry.getMetrics().get(2);
        System.out.println(AMC.toString());
        assertEquals(2, Cam.getMetricValue(), 0);

        ClassMetricsMessageSingle Cbm = (ClassMetricsMessageSingle) entry.getMetrics().get(3);
        assertEquals(3, Cbm.getMetricValue(), 0);

        ClassMetricsMessageSingle Dam = (ClassMetricsMessageSingle) entry.getMetrics().get(7);
        assertEquals(4, Dam.getMetricValue(), 0);

        ClassMetricsMessageSingle Dit = (ClassMetricsMessageSingle) entry.getMetrics().get(8);
        assertEquals(5, Dit.getMetricValue(), 0);

        ClassMetricsMessageSingle Lcom = (ClassMetricsMessageSingle) entry.getMetrics().get(9);
        assertEquals(6, Lcom.getMetricValue(), 0);

        ClassMetricsMessageSingle Rfc = (ClassMetricsMessageSingle) entry.getMetrics().get(17);
        assertEquals(7, Rfc.getMetricValue(), 0);
    }
}
