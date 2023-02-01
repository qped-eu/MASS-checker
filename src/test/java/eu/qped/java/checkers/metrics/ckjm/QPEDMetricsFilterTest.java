package eu.qped.java.checkers.metrics.ckjm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QPEDMetricsFilterTest {

    @Test
    public void testIncludeJdk() {
        QPEDMetricsFilter filter = new QPEDMetricsFilter(true, false);
        assertEquals(true, filter.isJdkIncluded());
    }

    @Test
    public void testIncludeAll() {
        QPEDMetricsFilter filter = new QPEDMetricsFilter(false, false);
        assertEquals(true, filter.includeAll());
    }

    @Test
    public void testProcessClass() {
        QPEDMetricsFilter filter = new QPEDMetricsFilter(false, false);
        List<String> files = new ArrayList<>();
        files.add("TestClass.class");
        filter.runMetricsInternal(files, null);
        // Add assertions to check if the class was processed as expected
    }

}