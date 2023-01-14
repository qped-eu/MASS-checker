package eu.qped.java.checkers.metrics.settings;

import eu.qped.java.checkers.mass.QfMetricsSettings;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import lombok.Builder;

import java.util.HashMap;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.*;

/**
 * Class representing a reader for the {@link QfMetricsSettings}'s input from the user.
 *
 * @author Jannik Seus
 */
@Builder
public class MetricSettingsReader {

    private QfMetricsSettings qfMetricsSettings;

    /**
     * Method reads the input Quarterfall design settings from {@link #qfMetricsSettings} and
     * checks (implicitly) for possible invalid values through the getter and parser.
     *
     * @param metricSettings the given Design Settings
     * @return the created {@link MetricSettings} object from an initial {@link QfMetricsSettings} object.
     */
    public MetricSettings readMetricsCheckerSettings(MetricSettings metricSettings) {

        metricSettings.includeCallsToJdk(Boolean.parseBoolean(qfMetricsSettings.areCallsToJdkIncluded()));
        metricSettings.includeOnlyPublicClasses(Boolean.parseBoolean(qfMetricsSettings.areOnlyPublicClassesIncluded()));
        metricSettings.setCustomSuggestions(readCustomSuggestions());
        metricSettings.setAmcConfig(createMetricConfig(AMC));
        metricSettings.setCaConfig(createMetricConfig(CA));
        metricSettings.setCamConfig(createMetricConfig(CAM));
        metricSettings.setCbmConfig(createMetricConfig(CBM));
        metricSettings.setCboConfig(createMetricConfig(CBO));
        metricSettings.setCcConfig(createMetricConfig(CC));
        metricSettings.setCeConfig(createMetricConfig(CE));
        metricSettings.setDamConfig(createMetricConfig(DAM));
        metricSettings.setDitConfig(createMetricConfig(DIT));
        metricSettings.setIcConfig(createMetricConfig(IC));
        metricSettings.setLcomConfig(createMetricConfig(LCOM));
        metricSettings.setLcom3Config(createMetricConfig(LCOM3));
        metricSettings.setLocConfig(createMetricConfig(LOC));
        metricSettings.setMfaConfig(createMetricConfig(MFA));
        metricSettings.setMoaConfig(createMetricConfig(MOA));
        metricSettings.setNocConfig(createMetricConfig(NOC));
        metricSettings.setNpmConfig(createMetricConfig(NPM));
        metricSettings.setRfcConfig(createMetricConfig(RFC));
        metricSettings.setWmcConfig(createMetricConfig(WMC));

        return metricSettings;
    }

    /**
     * Creates a {@link MetricConfig} object from a given metric.
     *
     * @param metric the given metric
     * @return the created {@link MetricConfig} object.
     * @throws IllegalArgumentException when the given parameter does not match any branch
     */
    private MetricConfig createMetricConfig(Metric metric) {
        switch (metric) {

            case AMC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getAmcSuggestionMin(), qfMetricsSettings.getAmcSuggestionMax()));
            case CA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCaSuggestionMin(), qfMetricsSettings.getCaSuggestionMax()));
            case CAM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCamSuggestionMin(), qfMetricsSettings.getCamSuggestionMax()));
            case CBM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCbmSuggestionMin(), qfMetricsSettings.getCbmSuggestionMax()));
            case CBO:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCboSuggestionMin(), qfMetricsSettings.getCboSuggestionMax()));
            case CC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCcSuggestionMin(), qfMetricsSettings.getCcSuggestionMax()));
            case CE:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCeSuggestionMin(), qfMetricsSettings.getCeSuggestionMax()));
            case DAM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getDamSuggestionMin(), qfMetricsSettings.getDamSuggestionMax()));
            case DIT:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getDitSuggestionMin(), qfMetricsSettings.getDitSuggestionMax()));
            case IC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getIcSuggestionMin(), qfMetricsSettings.getIcSuggestionMax()));
            case LCOM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLcomSuggestionMin(), qfMetricsSettings.getLcomSuggestionMax()));
            case LCOM3:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLcom3SuggestionMin(), qfMetricsSettings.getLcom3SuggestionMax()));
            case LOC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLocSuggestionMin(), qfMetricsSettings.getLocSuggestionMax()));
            case MFA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getMfaSuggestionMin(), qfMetricsSettings.getMfaSuggestionMax()));
            case MOA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getMoaSuggestionMin(), qfMetricsSettings.getMoaSuggestionMax()));
            case NOC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getNocSuggestionMin(), qfMetricsSettings.getNocSuggestionMax()));
            case NPM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getNpmSuggestionMin(), qfMetricsSettings.getNpmSuggestionMax()));
            case RFC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getRfcSuggestionMin(), qfMetricsSettings.getRfcSuggestionMax()));
            case WMC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getWmcSuggestionMin(), qfMetricsSettings.getWmcSuggestionMax()));
            default:
                throw new IllegalArgumentException("Illegal Metric type.");
        }
    }

    /**
     * Reads the custom suggestions for metrics (if set).
     *
     * @return the resulting map
     */
    private HashMap<Metric, MetricsFeedbackSuggestion> readCustomSuggestions() {
        HashMap<Metric, MetricsFeedbackSuggestion> suggestions = new HashMap<>();
        suggestions.put(AMC, new MetricsFeedbackSuggestion(qfMetricsSettings.getAmcSuggestionMin(), qfMetricsSettings.getAmcSuggestionMax()));
        suggestions.put(CA, new MetricsFeedbackSuggestion(qfMetricsSettings.getCaSuggestionMin(), qfMetricsSettings.getCaSuggestionMax()));
        suggestions.put(CAM, new MetricsFeedbackSuggestion(qfMetricsSettings.getCamSuggestionMin(), qfMetricsSettings.getCamSuggestionMax()));
        suggestions.put(CBM, new MetricsFeedbackSuggestion(qfMetricsSettings.getCbmSuggestionMin(), qfMetricsSettings.getCbmSuggestionMax()));
        suggestions.put(CBO, new MetricsFeedbackSuggestion(qfMetricsSettings.getCboSuggestionMin(), qfMetricsSettings.getCboSuggestionMax()));
        suggestions.put(CC, new MetricsFeedbackSuggestion(qfMetricsSettings.getCcSuggestionMin(), qfMetricsSettings.getCcSuggestionMax()));
        suggestions.put(CE, new MetricsFeedbackSuggestion(qfMetricsSettings.getCeSuggestionMin(), qfMetricsSettings.getCeSuggestionMax()));
        suggestions.put(DAM, new MetricsFeedbackSuggestion(qfMetricsSettings.getDamSuggestionMin(), qfMetricsSettings.getDamSuggestionMax()));
        suggestions.put(DIT, new MetricsFeedbackSuggestion(qfMetricsSettings.getDitSuggestionMin(), qfMetricsSettings.getDitSuggestionMax()));
        suggestions.put(IC, new MetricsFeedbackSuggestion(qfMetricsSettings.getIcSuggestionMin(), qfMetricsSettings.getIcSuggestionMax()));
        suggestions.put(LCOM, new MetricsFeedbackSuggestion(qfMetricsSettings.getLcomSuggestionMin(), qfMetricsSettings.getLcomSuggestionMax()));
        suggestions.put(LCOM3, new MetricsFeedbackSuggestion(qfMetricsSettings.getLcom3SuggestionMin(), qfMetricsSettings.getLcom3SuggestionMax()));
        suggestions.put(LOC, new MetricsFeedbackSuggestion(qfMetricsSettings.getLocSuggestionMin(), qfMetricsSettings.getLocSuggestionMax()));
        suggestions.put(MFA, new MetricsFeedbackSuggestion(qfMetricsSettings.getMfaSuggestionMin(), qfMetricsSettings.getMfaSuggestionMax()));
        suggestions.put(MOA, new MetricsFeedbackSuggestion(qfMetricsSettings.getMoaSuggestionMin(), qfMetricsSettings.getMoaSuggestionMax()));
        suggestions.put(NOC, new MetricsFeedbackSuggestion(qfMetricsSettings.getNocSuggestionMin(), qfMetricsSettings.getNocSuggestionMax()));
        suggestions.put(NPM, new MetricsFeedbackSuggestion(qfMetricsSettings.getNpmSuggestionMin(), qfMetricsSettings.getNpmSuggestionMax()));
        suggestions.put(RFC, new MetricsFeedbackSuggestion(qfMetricsSettings.getRfcSuggestionMin(), qfMetricsSettings.getRfcSuggestionMax()));
        suggestions.put(WMC, new MetricsFeedbackSuggestion(qfMetricsSettings.getWmcSuggestionMin(), qfMetricsSettings.getWmcSuggestionMax()));
        return suggestions;
    }

    /**
     * Retrieves the MetricThreshold of a given metric depending on set min and max values.
     *
     * @param metric the given metric
     * @return the created MetricThreshold
     */
    private MetricThreshold retrieveMetricThreshold(Metric metric) {
        double metricMinimum = retrieveMetricMinimum(metric);
        double metricMaximum = retrieveMetricMaximum(metric);
        boolean noMax = retrieveMetricNoMax(metric);

        return new MetricThreshold(metric, metricMinimum, metricMaximum, noMax);
    }

    /**
     * Retrieves the noMax Parameter of a given metric and parses it to a boolean value.
     *
     * @param metric the given metric
     * @return whether or no maximum is set for a given metric.
     */
    private boolean retrieveMetricNoMax(Metric metric) {
        String noMax = "false";

        switch (metric) {

            case AMC:
                noMax = qfMetricsSettings.getAmcNoMax();
                break;
            case CA:
                noMax = qfMetricsSettings.getCaNoMax();
                break;
            case CAM:
                noMax = qfMetricsSettings.getCamNoMax();
                break;
            case CBM:
                noMax = qfMetricsSettings.getCbmNoMax();
                break;
            case CBO:
                noMax = qfMetricsSettings.getCboNoMax();
                break;
            case CC:
                noMax = qfMetricsSettings.getCcNoMax();
                break;
            case CE:
                noMax = qfMetricsSettings.getCeNoMax();
                break;
            case DAM:
                noMax = qfMetricsSettings.getDamNoMax();
                break;
            case DIT:
                noMax = qfMetricsSettings.getDitNoMax();
                break;
            case IC:
                noMax = qfMetricsSettings.getIcNoMax();
                break;
            case LCOM:
                noMax = qfMetricsSettings.getLcomNoMax();
                break;
            case LCOM3:
                noMax = qfMetricsSettings.getLcom3NoMax();
                break;
            case LOC:
                noMax = qfMetricsSettings.getLocMax();
                break;
            case MFA:
                noMax = qfMetricsSettings.getMfaNoMax();
                break;
            case MOA:
                noMax = qfMetricsSettings.getMoaNoMax();
                break;
            case NOC:
                noMax = qfMetricsSettings.getNocNoMax();
                break;
            case NPM:
                noMax = qfMetricsSettings.getNpmNoMax();
                break;
            case RFC:
                noMax = qfMetricsSettings.getRfcNoMax();
                break;
            case WMC:
                noMax = qfMetricsSettings.getWmcNoMax();
                break;
        }
        return Boolean.parseBoolean(noMax);
    }

    /**
     * Helper to get the maximum value for a metric.
     *
     * @param metric given metric
     * @return the maximum threshold of a given metric, -1 if an exception occurred while parsing
     */
    private double retrieveMetricMaximum(Metric metric) {
        Double metricUpperThreshold = null;

        try {
            switch (metric) {
                case AMC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getAmcMax(), Double.MAX_VALUE);
                    break;
                case CAM:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCamMax(), Double.MAX_VALUE);
                    break;
                case CA:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCaMax(), Double.MAX_VALUE);
                    break;
                case CBM:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCbmMax(), Double.MAX_VALUE);
                    break;
                case CBO:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCboMax(), Double.MAX_VALUE);
                    break;
                case CC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCcMax(), Double.MAX_VALUE);
                    break;
                case CE:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getCeMax(), Double.MAX_VALUE);
                    break;
                case DAM:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getDamMax(), Double.MAX_VALUE);
                    break;
                case DIT:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getDitMax(), Double.MAX_VALUE);
                    break;
                case IC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getIcMax(), Double.MAX_VALUE);
                    break;
                case LCOM:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getLcomMax(), Double.MAX_VALUE);
                    break;
                case LCOM3:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getLcom3Max(), Double.MAX_VALUE);
                    break;
                case LOC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getLocMax(), Double.MAX_VALUE);
                    break;
                case MOA:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getMoaMax(), Double.MAX_VALUE);
                    break;
                case MFA:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getMfaMax(), Double.MAX_VALUE);
                    break;
                case NOC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getNocMax(), Double.MAX_VALUE);
                    break;
                case NPM:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getNpmMax(), Double.MAX_VALUE);
                    break;
                case RFC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getRfcMax(), Double.MAX_VALUE);
                    break;
                case WMC:
                    metricUpperThreshold = readDoubleOrElse(qfMetricsSettings.getWmcMax(), Double.MAX_VALUE);
                    break;
            }
        } catch (NumberFormatException | NullPointerException e) {
            metricUpperThreshold = -1d;
        }
        return metricUpperThreshold;
    }

    /**
     * Helper to get the minimum value for a metric.
     *
     * @param metric given metric
     * @return the maximum threshold of given metric, -1 if an exception occurred while parsing
     */
    private double retrieveMetricMinimum(Metric metric) {
        Double metricLowerThreshold = null;
        try {
            switch (metric) {
                case AMC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getAmcMin(), 0.0);
                    break;
                case CAM:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCamMin(), 0.0);
                    break;
                case CA:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCaMin(), 0.0);
                    break;
                case CBM:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCbmMin(), 0.0);
                    break;
                case CBO:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCboMin(), 0.0);
                    break;
                case CC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCcMin(), 0.0);
                    break;
                case CE:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getCeMin(), 0.0);
                    break;
                case DAM:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getDamMin(), 0.0);
                    break;
                case DIT:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getDitMin(), 0.0);
                    break;
                case IC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getIcMin(), 0.0);
                    break;
                case LCOM:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getLcomMin(), 0.0);
                    break;
                case LCOM3:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getLcom3Min(), 0.0);
                    break;
                case LOC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getLocMin(), 0.0);
                    break;
                case MOA:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getMoaMin(), 0.0);
                    break;
                case MFA:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getMfaMin(), 0.0);
                    break;
                case NOC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getNocMin(), 0.0);
                    break;
                case NPM:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getNpmMin(), 0.0);
                    break;
                case RFC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getRfcMin(), 0.0);
                    break;
                case WMC:
                    metricLowerThreshold = readDoubleOrElse(qfMetricsSettings.getWmcMin(), 0.0);
                    break;
            }
        } catch (NumberFormatException | NullPointerException e) {
            metricLowerThreshold = -1d;
        }
        return metricLowerThreshold;
    }

	private Double readDoubleOrElse(String string, double defaultValue) {
		if (string != null && string.isBlank()) {
			return defaultValue;
		}
		else {
			return Double.parseDouble(string);
		}
	}
}
