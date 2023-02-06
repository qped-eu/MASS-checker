package eu.qped.java.checkers.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackGenerator;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.settings.MetricSettings;

/**
 * Test class for {@link MetricsFeedback}.
 *
 * @author Jannik Seus
 */
class MetricsFeedbackTest {

    private MetricsFeedback metricsFeedback1;

    @BeforeEach
    void setUp() {
        metricsFeedback1 = MetricsFeedback.builder()
                .className("TestClass")
                .metric(Metric.AMC)
                .value(99d)
                .body(Metric.AMC.getDescription())
                .suggestion("Change something!")
                .build();
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateSuggestionTestMetric(Metric metric) {
        metricsFeedback1.setMetric(metric);
        metricsFeedback1.setBody(metric.getDescription());

        assertEquals(metric, metricsFeedback1.getMetric());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1d, 0d, 0.5d, 1.0d, 3.3d})
    void generateSuggestionTestValue(double value) {
        metricsFeedback1.setValue(value);
        metricsFeedback1.setSuggestion(metricsFeedback1.getSuggestion());

        assertEquals
                ("You are within the " + Metric.AMC + "'s threshold.",
                        MetricsFeedbackGenerator.generateDefaultSuggestion(
                                Metric.AMC, false, false));

        assertEquals
                ("The " + Metric.AMC + "'s value is too low: Increase your average method size, e.g. by joining multiple methods with mostly the same functionalities from over-engineering.",
                        MetricsFeedbackGenerator.generateDefaultSuggestion(
                                Metric.AMC, true, false));

        assertEquals
                ("The " + Metric.AMC + "'s value is too high: Decrease your average method size, e.g. by delegating functionalities to other newly created methods.",
                        MetricsFeedbackGenerator.generateDefaultSuggestion(
                                Metric.AMC, false, true));

        assertThrows(IllegalArgumentException.class,
                () -> MetricsFeedbackGenerator.generateDefaultSuggestion(
                        Metric.AMC,
                        true,
                        true));
        metricsFeedback1.setValue(99d);
        assertEquals(99d, metricsFeedback1.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "TestClass", "AnotherTestClass.java", "RandomName"})
    void generateSuggestionTest(String className) {
        metricsFeedback1.setClassName(className);
        assertEquals(className, metricsFeedback1.getClassName());

    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricsCheckerFeedbackTest() {
        MetricSettings metricSettings = MetricSettings.builder().build();
        List<ClassMetricsEntry> metricCheckerEntries =
                List.of(mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class),
                        mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class));

        assertEquals(MetricsFeedbackGenerator.generateMetricsCheckerFeedbacks(metricCheckerEntries, metricSettings), List.of());
    }

    @Test
    void testToString() {
        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());
        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());

        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());

        metricsFeedback1 = MetricsFeedback.builder().build();
        assertNotNull(metricsFeedback1);
    }
}