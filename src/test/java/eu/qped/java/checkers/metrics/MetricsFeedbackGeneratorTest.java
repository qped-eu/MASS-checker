package eu.qped.java.checkers.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;
import eu.qped.java.checkers.metrics.data.feedback.LowerAndUpperBound;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackGenerator;
import eu.qped.java.checkers.metrics.data.feedback.DefaultMetricSuggestion;
import eu.qped.java.checkers.metrics.settings.MetricSettings;

/**
 * Test class for {@link MetricsFeedbackGenerator}.
 *
 * @author Jannik Seus
 */
class MetricsFeedbackGeneratorTest {


    @ParameterizedTest
    @EnumSource(MetricCheckerEntryHandler.Metric.class)
    void generateMetricSpecificSuggestionLowerOrUpper(MetricCheckerEntryHandler.Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        assertEquals(getSuggestionByMetric(metric).getLowerBoundReachedSuggestion(), MetricsFeedbackGenerator.generateMetricSpecificSuggestionLower(metric));
        assertEquals(getSuggestionByMetric(metric).getUpperBoundReachedSuggestion(), MetricsFeedbackGenerator.generateMetricSpecificSuggestionUpper(metric));
    }



    // Method generateDesignFeedbacks() already covered in DesignCheckerTest.java

    private static DefaultMetricSuggestion getSuggestionByMetric(Metric metric) {
        switch (metric) {
            case AMC:
                return DefaultMetricSuggestion.AMC;
            case CA:
                return DefaultMetricSuggestion.CA;
            case CAM:
                return DefaultMetricSuggestion.CAM;
            case CBM:
                return DefaultMetricSuggestion.CBM;
            case CBO:
                return DefaultMetricSuggestion.CBO;
            case CC:
                return DefaultMetricSuggestion.CC;
            case CE:
                return DefaultMetricSuggestion.CE;
            case DAM:
                return DefaultMetricSuggestion.DAM;
            case DIT:
                return DefaultMetricSuggestion.DIT;
            case IC:
                return DefaultMetricSuggestion.IC;
            case LCOM:
                return DefaultMetricSuggestion.LCOM;
            case LCOM3:
                return DefaultMetricSuggestion.LCOM3;
            case LOC:
                return DefaultMetricSuggestion.LOC;
            case MFA:
                return DefaultMetricSuggestion.MFA;
            case MOA:
                return DefaultMetricSuggestion.MOA;
            case NOC:
                return DefaultMetricSuggestion.NOC;
            case NPM:
                return DefaultMetricSuggestion.NPM;
            case RFC:
                return DefaultMetricSuggestion.RFC;
            case WMC:
                return DefaultMetricSuggestion.WMC;
        }
        return null;
    }

}