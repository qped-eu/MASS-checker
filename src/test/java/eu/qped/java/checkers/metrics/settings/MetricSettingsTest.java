package eu.qped.java.checkers.metrics.settings;

import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import eu.qped.java.checkers.metrics.utils.MetricsCheckerTestUtility;
import eu.qped.java.checkers.mass.QFMetricsSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Represents a test class for {@link MetricSettings}.
 *
 * @author Jannik Seus
 */
class MetricSettingsTest {

    private MetricSettings metricSettings;

    @BeforeEach
    void setUp() {
        this.metricSettings = MetricSettings.builder().build();
    }

    @Test
    void setQfMetricsSettingsTest() {
        QFMetricsSettings qfMetricsSettings = MetricsCheckerTestUtility.generateQMetricsSettings();

        metricSettings.setAmcConfig(new MetricConfig(new MetricThreshold(Metric.AMC, Double.parseDouble(qfMetricsSettings.getAmcMin()), Double.parseDouble(qfMetricsSettings.getAmcMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getAmcConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getAmcConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));
        assertEquals(qfMetricsSettings.getAmcSuggestionMax(), "AMC CUSTOM SUGGESTION UPPER BOUND");

        metricSettings.setCaConfig(new MetricConfig(new MetricThreshold(Metric.CA, Double.parseDouble(qfMetricsSettings.getCaMin()), Double.parseDouble(qfMetricsSettings.getCaMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCaConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCaConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));


        metricSettings.setCamConfig(new MetricConfig(new MetricThreshold(Metric.CAM, Double.parseDouble(qfMetricsSettings.getCaMin()), Double.parseDouble(qfMetricsSettings.getCaMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCamConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCamConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setCbmConfig(new MetricConfig(new MetricThreshold(Metric.CBM, Double.parseDouble(qfMetricsSettings.getCbmMin()), Double.parseDouble(qfMetricsSettings.getCbmMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCbmConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCbmConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setCboConfig(new MetricConfig(new MetricThreshold(Metric.CBO, Double.parseDouble(qfMetricsSettings.getCboMin()), Double.parseDouble(qfMetricsSettings.getCboMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCboConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCboConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setCcConfig(new MetricConfig(new MetricThreshold(Metric.CC, Double.parseDouble(qfMetricsSettings.getCcMin()), Double.parseDouble(qfMetricsSettings.getCcMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCcConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCcConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));
        assertEquals(qfMetricsSettings.getCcSuggestionMax(), "CC CUSTOM SUGGESTION UPPER BOUND");

        metricSettings.setCeConfig(new MetricConfig(new MetricThreshold(Metric.CE, Double.parseDouble(qfMetricsSettings.getCeMin()), Double.parseDouble(qfMetricsSettings.getCeMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getCeConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getCeConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setDamConfig(new MetricConfig(new MetricThreshold(Metric.DAM, Double.parseDouble(qfMetricsSettings.getDamMin()), Double.parseDouble(qfMetricsSettings.getDamMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getDamConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getDamConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setDitConfig(new MetricConfig(new MetricThreshold(Metric.DIT, Double.parseDouble(qfMetricsSettings.getDitMin()), Double.parseDouble(qfMetricsSettings.getDitMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getDitConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getDitConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setIcConfig(new MetricConfig(new MetricThreshold(Metric.IC, Double.parseDouble(qfMetricsSettings.getIcMin()), Double.parseDouble(qfMetricsSettings.getIcMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getIcConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getIcConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setLcomConfig(new MetricConfig(new MetricThreshold(Metric.LCOM, Double.parseDouble(qfMetricsSettings.getLcomMin()), Double.parseDouble(qfMetricsSettings.getLcomMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getLcomConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getLcomConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setLcom3Config(new MetricConfig(new MetricThreshold(Metric.LCOM3, Double.parseDouble(qfMetricsSettings.getLcom3Min()), Double.parseDouble(qfMetricsSettings.getLcom3Max()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getLcom3Config().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getLcom3Config().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));
        assertEquals(qfMetricsSettings.getLcom3SuggestionMin(), "LCOM3 CUSTOM SUGGESTION LOWER BOUND");

        metricSettings.setLocConfig(new MetricConfig(new MetricThreshold(Metric.LOC, Double.parseDouble(qfMetricsSettings.getLocMin()), Double.parseDouble(qfMetricsSettings.getLocMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getLocConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getLocConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setMfaConfig(new MetricConfig(new MetricThreshold(Metric.MFA, Double.parseDouble(qfMetricsSettings.getMfaMin()), Double.parseDouble(qfMetricsSettings.getMfaMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getMfaConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getMfaConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setMoaConfig(new MetricConfig(new MetricThreshold(Metric.MOA, Double.parseDouble(qfMetricsSettings.getMoaMin()), Double.parseDouble(qfMetricsSettings.getMoaMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getMoaConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getMoaConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setNocConfig(new MetricConfig(new MetricThreshold(Metric.NOC, Double.parseDouble(qfMetricsSettings.getNocMin()), Double.parseDouble(qfMetricsSettings.getNocMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getNocConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getNocConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setNpmConfig(new MetricConfig(new MetricThreshold(Metric.NPM, Double.parseDouble(qfMetricsSettings.getNpmMin()), Double.parseDouble(qfMetricsSettings.getNpmMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getNpmConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getNpmConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setRfcConfig(new MetricConfig(new MetricThreshold(Metric.RFC, Double.parseDouble(qfMetricsSettings.getRfcMin()), Double.parseDouble(qfMetricsSettings.getRfcMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getRfcConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getRfcConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        metricSettings.setWmcConfig(new MetricConfig(new MetricThreshold(Metric.WMC, Double.parseDouble(qfMetricsSettings.getWmcMin()), Double.parseDouble(qfMetricsSettings.getWmcMax()), false), new MetricsFeedbackSuggestion("lower", "upper")));
        assertThat(metricSettings.getWmcConfig().getMetricThreshold().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(metricSettings.getWmcConfig().getMetricThreshold().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));
        assertEquals(qfMetricsSettings.getWmcSuggestionMin(), "WMC CUSTOM SUGGESTION LOWER BOUND");
    }
}