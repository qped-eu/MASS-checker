package eu.qped.java.checkers.metrics.settings;

import eu.qped.java.checkers.mass.QFMetricsSettings;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import lombok.Builder;

import java.util.HashMap;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.*;

/**
 * Class representing a reader for the {@link QFMetricsSettings}'s input from the user.
 *
 * @author Jannik Seus
 */
@Builder
public class MetricSettingsReader {

    private QFMetricsSettings qfMetricsSettings;

    /**
     * Method reads the input Quarterfall design settings from {@link #qfMetricsSettings} and
     * checks (implicitly) for possible invalid values through the getter and parser.
     *
     * @param metricSettings the given Design Settings
     * @return the created {@link MetricSettings} object from an initial {@link QFMetricsSettings} object.
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
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getAmcCustomSuggestionLower(), qfMetricsSettings.getAmcCustomSuggestionUpper()));
            case CA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCaCustomSuggestionLower(), qfMetricsSettings.getCaCustomSuggestionUpper()));
            case CAM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCamCustomSuggestionLower(), qfMetricsSettings.getCamCustomSuggestionUpper()));
            case CBM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCbmCustomSuggestionLower(), qfMetricsSettings.getCbmCustomSuggestionUpper()));
            case CBO:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCboCustomSuggestionLower(), qfMetricsSettings.getCboCustomSuggestionUpper()));
            case CC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCcCustomSuggestionLower(), qfMetricsSettings.getCcCustomSuggestionUpper()));
            case CE:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getCeCustomSuggestionLower(), qfMetricsSettings.getCeCustomSuggestionUpper()));
            case DAM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getDamCustomSuggestionLower(), qfMetricsSettings.getDamCustomSuggestionUpper()));
            case DIT:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getDitCustomSuggestionLower(), qfMetricsSettings.getDitCustomSuggestionUpper()));
            case IC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getIcCustomSuggestionLower(), qfMetricsSettings.getIcCustomSuggestionUpper()));
            case LCOM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLcomCustomSuggestionLower(), qfMetricsSettings.getLcomCustomSuggestionUpper()));
            case LCOM3:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLcom3CustomSuggestionLower(), qfMetricsSettings.getLcom3CustomSuggestionUpper()));
            case LOC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getLocCustomSuggestionLower(), qfMetricsSettings.getLocCustomSuggestionUpper()));
            case MFA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getMfaCustomSuggestionLower(), qfMetricsSettings.getMfaCustomSuggestionUpper()));
            case MOA:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getMoaCustomSuggestionLower(), qfMetricsSettings.getMoaCustomSuggestionUpper()));
            case NOC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getNocCustomSuggestionLower(), qfMetricsSettings.getNocCustomSuggestionUpper()));
            case NPM:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getNpmCustomSuggestionLower(), qfMetricsSettings.getNpmCustomSuggestionUpper()));
            case RFC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getRfcCustomSuggestionLower(), qfMetricsSettings.getRfcCustomSuggestionUpper()));
            case WMC:
                return new MetricConfig(retrieveMetricThreshold(metric), new MetricsFeedbackSuggestion(qfMetricsSettings.getWmcCustomSuggestionLower(), qfMetricsSettings.getWmcCustomSuggestionUpper()));
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
        suggestions.put(AMC, new MetricsFeedbackSuggestion(qfMetricsSettings.getAmcCustomSuggestionLower(), qfMetricsSettings.getAmcCustomSuggestionUpper()));
        suggestions.put(CA, new MetricsFeedbackSuggestion(qfMetricsSettings.getCaCustomSuggestionLower(), qfMetricsSettings.getCaCustomSuggestionUpper()));
        suggestions.put(CAM, new MetricsFeedbackSuggestion(qfMetricsSettings.getCamCustomSuggestionLower(), qfMetricsSettings.getCamCustomSuggestionUpper()));
        suggestions.put(CBM, new MetricsFeedbackSuggestion(qfMetricsSettings.getCbmCustomSuggestionLower(), qfMetricsSettings.getCbmCustomSuggestionUpper()));
        suggestions.put(CBO, new MetricsFeedbackSuggestion(qfMetricsSettings.getCboCustomSuggestionLower(), qfMetricsSettings.getCboCustomSuggestionUpper()));
        suggestions.put(CC, new MetricsFeedbackSuggestion(qfMetricsSettings.getCcCustomSuggestionLower(), qfMetricsSettings.getCcCustomSuggestionUpper()));
        suggestions.put(CE, new MetricsFeedbackSuggestion(qfMetricsSettings.getCeCustomSuggestionLower(), qfMetricsSettings.getCeCustomSuggestionUpper()));
        suggestions.put(DAM, new MetricsFeedbackSuggestion(qfMetricsSettings.getDamCustomSuggestionLower(), qfMetricsSettings.getDamCustomSuggestionUpper()));
        suggestions.put(DIT, new MetricsFeedbackSuggestion(qfMetricsSettings.getDitCustomSuggestionLower(), qfMetricsSettings.getDitCustomSuggestionUpper()));
        suggestions.put(IC, new MetricsFeedbackSuggestion(qfMetricsSettings.getIcCustomSuggestionLower(), qfMetricsSettings.getIcCustomSuggestionUpper()));
        suggestions.put(LCOM, new MetricsFeedbackSuggestion(qfMetricsSettings.getLcomCustomSuggestionLower(), qfMetricsSettings.getLcomCustomSuggestionUpper()));
        suggestions.put(LCOM3, new MetricsFeedbackSuggestion(qfMetricsSettings.getLcom3CustomSuggestionLower(), qfMetricsSettings.getLcom3CustomSuggestionUpper()));
        suggestions.put(LOC, new MetricsFeedbackSuggestion(qfMetricsSettings.getLocCustomSuggestionLower(), qfMetricsSettings.getLocCustomSuggestionUpper()));
        suggestions.put(MFA, new MetricsFeedbackSuggestion(qfMetricsSettings.getMfaCustomSuggestionLower(), qfMetricsSettings.getMfaCustomSuggestionUpper()));
        suggestions.put(MOA, new MetricsFeedbackSuggestion(qfMetricsSettings.getMoaCustomSuggestionLower(), qfMetricsSettings.getMoaCustomSuggestionUpper()));
        suggestions.put(NOC, new MetricsFeedbackSuggestion(qfMetricsSettings.getNocCustomSuggestionLower(), qfMetricsSettings.getNocCustomSuggestionUpper()));
        suggestions.put(NPM, new MetricsFeedbackSuggestion(qfMetricsSettings.getNpmCustomSuggestionLower(), qfMetricsSettings.getNpmCustomSuggestionUpper()));
        suggestions.put(RFC, new MetricsFeedbackSuggestion(qfMetricsSettings.getRfcCustomSuggestionLower(), qfMetricsSettings.getRfcCustomSuggestionUpper()));
        suggestions.put(WMC, new MetricsFeedbackSuggestion(qfMetricsSettings.getWmcCustomSuggestionLower(), qfMetricsSettings.getWmcCustomSuggestionUpper()));
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
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getAmcMax());
                    break;
                case CAM:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCamMax());
                    break;
                case CA:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCaMax());
                    break;
                case CBM:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCbmMax());
                    break;
                case CBO:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCboMax());
                    break;
                case CC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCcMax());
                    break;
                case CE:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getCeMax());
                    break;
                case DAM:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getDamMax());
                    break;
                case DIT:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getDitMax());
                    break;
                case IC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getIcMax());
                    break;
                case LCOM:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getLcomMax());
                    break;
                case LCOM3:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getLcom3Max());
                    break;
                case LOC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getLocMax());
                    break;
                case MOA:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getMoaMax());
                    break;
                case MFA:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getMfaMax());
                    break;
                case NOC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getNocMax());
                    break;
                case NPM:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getNpmMax());
                    break;
                case RFC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getRfcMax());
                    break;
                case WMC:
                    metricUpperThreshold = Double.parseDouble(qfMetricsSettings.getWmcMax());
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
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getAmcMin());
                    break;
                case CAM:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCamMin());
                    break;
                case CA:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCaMin());
                    break;
                case CBM:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCbmMin());
                    break;
                case CBO:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCboMin());
                    break;
                case CC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCcMin());
                    break;
                case CE:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getCeMin());
                    break;
                case DAM:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getDamMin());
                    break;
                case DIT:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getDitMin());
                    break;
                case IC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getIcMin());
                    break;
                case LCOM:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getLcomMin());
                    break;
                case LCOM3:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getLcom3Min());
                    break;
                case LOC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getLocMin());
                    break;
                case MOA:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getMoaMin());
                    break;
                case MFA:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getMfaMin());
                    break;
                case NOC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getNocMin());
                    break;
                case NPM:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getNpmMin());
                    break;
                case RFC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getRfcMin());
                    break;
                case WMC:
                    metricLowerThreshold = Double.parseDouble(qfMetricsSettings.getWmcMin());
                    break;
            }
        } catch (NumberFormatException | NullPointerException e) {
            metricLowerThreshold = -1d;
        }
        return metricLowerThreshold;
    }
}
