package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.metrics.MetricsChecker;
import lombok.Getter;
import lombok.Setter;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric.*;

/**
 * Data class modeling metrics settings for {@link MetricsChecker}.
 * The fields are used to hold the string values from an input json file.
 * They are modeling the thresholds for the corresponding metric, custom suggestions and other settings for the checker.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
public class QFMetricsSettings extends QfObjectBase {

    private String includeCallsToJdk = "false";
    private String includeOnlyPublicClasses = "false";

    /**
     * Average method Complexity
     */
    private String amcMin;
    private String amcMax;
    private String amcNoMax;
    private String amcCustomSuggestionLower;
    private String amcCustomSuggestionUpper;

    /**
     * Afferent coupled classes: classes that use this class
     */
    private String caMin;
    private String caMax;
    private String caNoMax;
    private String caCustomSuggestionLower;
    private String caCustomSuggestionUpper;

    /**
     * Cohesion Among methods of Class
     */
    private String camMin;
    private String camMax;
    private String camNoMax;
    private String camCustomSuggestionLower;
    private String camCustomSuggestionUpper;


    /**
     * Coupling Between methods
     */
    private String cbmMin;
    private String cbmMax;
    private String cbmNoMax;
    private String cbmCustomSuggestionLower;
    private String cbmCustomSuggestionUpper;

    /**
     * Coupling between object classes
     */
    private String cboMin;
    private String cboMax;
    private String cboNoMax;
    private String cboCustomSuggestionLower;
    private String cboCustomSuggestionUpper;

    /**
     * Signatures of methods and values of McCabe Cyclomatic Complexity
     */
    private String ccMin;
    private String ccMax;
    private String ccNoMax;
    private String ccCustomSuggestionLower;
    private String ccCustomSuggestionUpper;

    /**
     * Coupled classes: classes being used by this class
     */
    private String ceMin;
    private String ceMax;
    private String ceNoMax;
    private String ceCustomSuggestionLower;
    private String ceCustomSuggestionUpper;

    /**
     * Data Access metric
     */
    private String damMin;
    private String damMax;
    private String damNoMax;
    private String damCustomSuggestionLower;
    private String damCustomSuggestionUpper;


    /**
     * Depth of inheritance tree
     */
    private String ditMin;
    private String ditMax;
    private String ditNoMax;
    private String ditCustomSuggestionLower;
    private String ditCustomSuggestionUpper;

    /**
     * Inheritance Coupling
     */
    private String icMin;
    private String icMax;
    private String icNoMax;
    private String icCustomSuggestionLower;
    private String icCustomSuggestionUpper;

    /**
     * Lack of cohesion in methods
     */
    private String lcomMin;
    private String lcomMax;
    private String lcomNoMax;
    private String lcomCustomSuggestionLower;
    private String lcomCustomSuggestionUpper;

    /**
     * Lack of cohesion in methods - Henderson-Sellers definition
     */
    private String lcom3Min;
    private String lcom3Max;
    private String lcom3NoMax;
    private String lcom3CustomSuggestionLower;
    private String lcom3CustomSuggestionUpper;

    /**
     * Line of codes per class (minimum and maximum thresholds)
     */
    private String locMin;
    private String locMax;
    private String locNoMax;
    private String locCustomSuggestionLower;
    private String locCustomSuggestionUpper;

    /**
     * measure of Aggregation
     */
    private String moaMin;
    private String moaMax;
    private String moaNoMax;
    private String moaCustomSuggestionLower;
    private String moaCustomSuggestionUpper;

    /**
     * measure of Functional Abstraction
     */
    private String mfaMin;
    private String mfaMax;
    private String mfaNoMax;
    private String mfaCustomSuggestionLower;
    private String mfaCustomSuggestionUpper;

    /**
     * Number of children
     */
    private String nocMin;
    private String nocMax;
    private String nocNoMax;
    private String nocCustomSuggestionLower;
    private String nocCustomSuggestionUpper;

    /**
     * Number of public methods
     */
    private String npmMin;
    private String npmMax;
    private String npmNoMax;
    private String npmCustomSuggestionLower;
    private String npmCustomSuggestionUpper;

    /**
     * Response for a Class
     */
    private String rfcMin;
    private String rfcMax;
    private String rfcNoMax;
    private String rfcCustomSuggestionLower;
    private String rfcCustomSuggestionUpper;

    /**
     * Weighted methods per class
     */
    private String wmcMin;
    private String wmcMax;
    private String wmcNoMax;
    private String wmcCustomSuggestionLower;
    private String wmcCustomSuggestionUpper;

    public String areCallsToJdkIncluded() {
        return includeCallsToJdk;
    }

    public void includeCallsToJdk(String includeCallsToJdk) {
        this.includeCallsToJdk = includeCallsToJdk;
    }

    public String areOnlyPublicClassesIncluded() {
        return includeOnlyPublicClasses;
    }

    public void includeOnlyPublicClasses(String includeOnlyPublicClasses) {
        this.includeOnlyPublicClasses = includeOnlyPublicClasses;
    }

    public void setLoc(String locMin, String locMax) {
        setLocMin(locMin);
        setLocMax(locMax);
    }

    public void setWmc(String wmcMin, String wmcMax) {
        setWmcMin(wmcMin);
        setWmcMax(wmcMax);
    }

    public void setNoc(String nocMin, String nocMax) {
        setNocMin(nocMin);
        setNocMax(nocMax);
    }

    public void setRfc(String rfcMin, String rfcMax) {
        setRfcMin(rfcMin);
        setRfcMax(rfcMax);
    }

    public void setCe(String ceMin, String ceMax) {
        setCeMin(ceMin);
        setCeMax(ceMax);
    }

    public void setCa(String caMin, String caMax) {
        setCaMin(caMin);
        setCaMax(caMax);
    }

    public void setDit(String ditMin, String ditMax) {
        setDitMin(ditMin);
        setDitMax(ditMax);
    }

    public void setLcom(String lcomMin, String lcomMax) {
        setLcomMin(lcomMin);
        setLcomMax(lcomMax);
    }

    public void setLcom3(String lcom3Min, String lcom3Max) {
        setLcom3Min(lcom3Min);
        setLcom3Max(lcom3Max);
    }

    public void setNpm(String npmMin, String npmMax) {
        setNpmMin(npmMin);
        setNpmMax(npmMax);
    }

    public void setCc(String ccMin, String ccMax) {
        setCcMin(ccMin);
        setCcMax(ccMax);
    }

    public void setDam(String damMin, String damMax) {
        setDamMin(damMin);
        setDamMax(damMax);
    }

    public void setMoa(String moaMin, String moaMax) {
        setMoaMin(moaMin);
        setMoaMax(moaMax);
    }

    public void setMfa(String mfaMin, String mfaMax) {
        setMfaMin(mfaMin);
        setMfaMax(mfaMax);
    }

    public void setCam(String camMin, String camMax) {
        setCamMin(camMin);
        setCamMax(camMax);
    }

    public void setIc(String icMin, String icMax) {
        setIcMin(icMin);
        setIcMax(icMax);
    }

    public void setCbm(String cbmMin, String cbmMax) {
        setCbmMin(cbmMin);
        setCbmMax(cbmMax);
    }

    public void setAmc(String amcMin, String amcMax) {
        setAmcMin(amcMin);
        setAmcMax(amcMax);
    }

    public void setCbo(String cboMin, String cboMax) {
        setCboMin(cboMin);
        setCboMax(cboMax);
    }
}