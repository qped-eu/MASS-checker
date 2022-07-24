package eu.qped.java.checkers.metrics.data;

import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.data.report.MetricsCheckerReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link MetricsCheckerReport}.
 *
 * @author Jannik Seus
 */
class MetricsCheckerReportTest {

    private final ClassMetricsEntry mockedClassMetricsEntry = mock(ClassMetricsEntry.class);
    private final String samplePath = "path";
    private MetricsCheckerReport metricsCheckerReport;

    @BeforeEach
    void setUp() {
        metricsCheckerReport = MetricsCheckerReport.builder()
                .metricsMap(List.of(mockedClassMetricsEntry))
                .pathsToClassFiles(List.of(samplePath))
                .build();
    }

    @Test
    void getPathsToClassFiles() {
        assertEquals(1, metricsCheckerReport.getPathsToClassFiles().size());
        assertTrue(metricsCheckerReport.getPathsToClassFiles().contains(samplePath));

    }

    @Test
    void getMetricsMap() {
        assertEquals(1, metricsCheckerReport.getMetricsMap().size());
        assertTrue(metricsCheckerReport.getMetricsMap().contains(mockedClassMetricsEntry));

    }

    @Test
    void setPathsToClassFiles() {
        String mocked = "anotherPath";
        metricsCheckerReport.setPathsToClassFiles(List.of(mocked));
        assertEquals(1, metricsCheckerReport.getPathsToClassFiles().size());
        assertTrue(metricsCheckerReport.getPathsToClassFiles().contains(mocked));
    }

    @Test
    void setMetricsMap() {
        ClassMetricsEntry mocked = mock(ClassMetricsEntry.class);
        metricsCheckerReport.setMetricsMap(List.of(mocked));
        assertEquals(1, metricsCheckerReport.getMetricsMap().size());
        assertTrue(metricsCheckerReport.getMetricsMap().contains(mocked));



    }
}