package eu.qped.java.checkers.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.metrics.MetricsChecker;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
    private String amcSuggestionMin;
    private String amcSuggestionMax;

    @JsonProperty("amcThreshold")
    private void amcDeserializer(Map<String, Object> amc) {
        this.amcMin = String.valueOf(String.valueOf(amc.get("min")));
        this.amcMax = String.valueOf(String.valueOf(amc.get("max")));
        this.amcNoMax = String.valueOf(amc.get("noMax"));
        this.amcSuggestionMin = String.valueOf(amc.get("suggestionMin"));
        this.amcSuggestionMax = String.valueOf(amc.get("suggestionMax"));
    }

    /**
     * Afferent coupled classes: classes that use this class
     */
    private String caMin;
    private String caMax;
    private String caNoMax;
    private String caSuggestionMin;
    private String caSuggestionMax;

    @JsonProperty("caThreshold")
    private void caDeserializer(Map<String, Object> ca) {
        this.caMin = String.valueOf(ca.get("min"));
        this.caMax = String.valueOf(ca.get("max"));
        this.caNoMax = String.valueOf(ca.get("noMax"));
        this.caSuggestionMin = String.valueOf(ca.get("suggestionMin"));
        this.caSuggestionMax = String.valueOf(ca.get("suggestionMax"));
    }

    /**
     * Cohesion Among methods of Class
     */
    private String camMin;
    private String camMax;
    private String camNoMax;
    private String camSuggestionMin;
    private String camSuggestionMax;

    @JsonProperty("camThreshold")
    private void camDeserializer(Map<String, Object> cam) {
        this.camMin = String.valueOf(cam.get("min"));
        this.camMax = String.valueOf(cam.get("max"));
        this.camNoMax = String.valueOf(cam.get("noMax"));
        this.camSuggestionMin = String.valueOf(cam.get("suggestionMin"));
        this.camSuggestionMax = String.valueOf(cam.get("suggestionMax"));
    }

    /**
     * Coupling Between methods
     */
    private String cbmMin;
    private String cbmMax;
    private String cbmNoMax;
    private String cbmSuggestionMin;
    private String cbmSuggestionMax;

    @JsonProperty("cbmThreshold")
    private void cbmDeserializer(Map<String, Object> cbm) {
        this.cbmMin = String.valueOf(cbm.get("min"));
        this.cbmMax = String.valueOf(cbm.get("max"));
        this.cbmNoMax = String.valueOf(cbm.get("noMax"));
        this.cbmSuggestionMin = String.valueOf(cbm.get("suggestionMin"));
        this.cbmSuggestionMax = String.valueOf(cbm.get("suggestionMax"));
    }

    /**
     * Coupling between object classes
     */
    private String cboMin;
    private String cboMax;
    private String cboNoMax;
    private String cboSuggestionMin;
    private String cboSuggestionMax;

    @JsonProperty("cboThreshold")
    private void cboDeserializer(Map<String, Object> cbo) {
        this.cboMin = String.valueOf(cbo.get("min"));
        this.cboMax = String.valueOf(cbo.get("max"));
        this.cboNoMax = String.valueOf(cbo.get("noMax"));
        this.cboSuggestionMin = String.valueOf(cbo.get("suggestionMin"));
        this.cboSuggestionMax = String.valueOf(cbo.get("suggestionMax"));
    }

    /**
     * Signatures of methods and values of McCabe Cyclomatic Complexity
     */
    private String ccMin;
    private String ccMax;
    private String ccNoMax;
    private String ccSuggestionMin;
    private String ccSuggestionMax;

    @JsonProperty("ccThreshold")
    private void ccDeserializer(Map<String, Object> cc) {
        this.ccMin = String.valueOf(cc.get("min"));
        this.ccMax = String.valueOf(cc.get("max"));
        this.ccNoMax = String.valueOf(cc.get("noMax"));
        this.ccSuggestionMin = String.valueOf(cc.get("suggestionMin"));
        this.ccSuggestionMax = String.valueOf(cc.get("suggestionMax"));
    }

    /**
     * Coupled classes: classes being used by this class
     */
    private String ceMin;
    private String ceMax;
    private String ceNoMax;
    private String ceSuggestionMin;
    private String ceSuggestionMax;

    @JsonProperty("ceThreshold")
    private void ceDeserializer(Map<String, Object> ce) {
        this.ceMin = String.valueOf(ce.get("min"));
        this.ceMax = String.valueOf(ce.get("max"));
        this.ceNoMax = String.valueOf(ce.get("noMax"));
        this.ceSuggestionMin = String.valueOf(ce.get("suggestionMin"));
        this.ceSuggestionMax = String.valueOf(ce.get("suggestionMax"));
    }

    /**
     * Data Access metric
     */
    private String damMin;
    private String damMax;
    private String damNoMax;
    private String damSuggestionMin;
    private String damSuggestionMax;

    @JsonProperty("damThreshold")
    private void damDeserializer(Map<String, Object> dam) {
        this.damMin = String.valueOf(dam.get("min"));
        this.damMax = String.valueOf(dam.get("max"));
        this.damNoMax = String.valueOf(dam.get("noMax"));
        this.damSuggestionMin = String.valueOf(dam.get("suggestionMin"));
        this.damSuggestionMax = String.valueOf(dam.get("suggestionMax"));
    }

    /**
     * Depth of inheritance tree
     */
    private String ditMin;
    private String ditMax;
    private String ditNoMax;
    private String ditSuggestionMin;
    private String ditSuggestionMax;

    @JsonProperty("ditThreshold")
    private void ditDeserializer(Map<String, Object> dit) {
        this.ditMin = String.valueOf(dit.get("min"));
        this.ditMax = String.valueOf(dit.get("max"));
        this.ditNoMax = String.valueOf(dit.get("noMax"));
        this.ditSuggestionMin = String.valueOf(dit.get("suggestionMin"));
        this.ditSuggestionMax = String.valueOf(dit.get("suggestionMax"));
    }

    /**
     * Inheritance Coupling
     */
    private String icMin;
    private String icMax;
    private String icNoMax;
    private String icSuggestionMin;
    private String icSuggestionMax;

    @JsonProperty("icThreshold")
    private void icDeserializer(Map<String, Object> ic) {
        this.icMin = String.valueOf(ic.get("min"));
        this.icMax = String.valueOf(ic.get("max"));
        this.icNoMax = String.valueOf(ic.get("noMax"));
        this.icSuggestionMin = String.valueOf(ic.get("suggestionMin"));
        this.icSuggestionMax = String.valueOf(ic.get("suggestionMax"));
    }

    /**
     * Lack of cohesion in methods
     */
    private String lcomMin;
    private String lcomMax;
    private String lcomNoMax;
    private String lcomSuggestionMin;
    private String lcomSuggestionMax;

    @JsonProperty("lcomThreshold")
    private void lcomDeserializer(Map<String, Object> lcom) {
        this.lcomMin = String.valueOf(lcom.get("min"));
        this.lcomMax = String.valueOf(lcom.get("max"));
        this.lcomNoMax = String.valueOf(lcom.get("noMax"));
        this.lcomSuggestionMin = String.valueOf(lcom.get("suggestionMin"));
        this.lcomSuggestionMax = String.valueOf(lcom.get("suggestionMax"));
    }

    /**
     * Lack of cohesion in methods - Henderson-Sellers definition
     */
    private String lcom3Min;
    private String lcom3Max;
    private String lcom3NoMax;
    private String lcom3SuggestionMin;
    private String lcom3SuggestionMax;

    @JsonProperty("lcom3Threshold")
    private void lcom3Deserializer(Map<String, Object> lcom3) {
        this.lcom3Min = String.valueOf(lcom3.get("min"));
        this.lcom3Max = String.valueOf(lcom3.get("max"));
        this.lcom3NoMax = String.valueOf(lcom3.get("noMax"));
        this.lcom3SuggestionMin = String.valueOf(lcom3.get("suggestionMin"));
        this.lcom3SuggestionMax = String.valueOf(lcom3.get("suggestionMax"));
    }

    /**
     * Line of codes per class (minimum and maximum thresholds)
     */
    private String locMin;
    private String locMax;
    private String locNoMax;
    private String locSuggestionMin;
    private String locSuggestionMax;

    @JsonProperty("locThreshold")
    private void locDeserializer(Map<String, Object> loc) {
        this.locMin = String.valueOf(loc.get("min"));
        this.locMax = String.valueOf(loc.get("max"));
        this.locNoMax = String.valueOf(loc.get("noMax"));
        this.locSuggestionMin= String.valueOf(loc.get("suggestionMin"));
        this.locSuggestionMax= String.valueOf(loc.get("suggestionMax"));
    }

    /**
     * measure of Aggregation
     */
    private String moaMin;
    private String moaMax;
    private String moaNoMax;
    private String moaSuggestionMin;
    private String moaSuggestionMax;

    @JsonProperty("moaThreshold")
    private void moaDeserializer(Map<String, Object> moa) {
        this.moaMin = String.valueOf(moa.get("min"));
        this.moaMax = String.valueOf(moa.get("max"));
        this.moaNoMax = String.valueOf(moa.get("noMax"));
        this.moaSuggestionMin = String.valueOf(moa.get("suggestionMin"));
        this.moaSuggestionMax = String.valueOf(moa.get("suggestionMax"));
    }

    /**
     * measure of Functional Abstraction
     */
    private String mfaMin;
    private String mfaMax;
    private String mfaNoMax;
    private String mfaSuggestionMin;
    private String mfaSuggestionMax;

    @JsonProperty("mfaThreshold")
    private void mfaDeserializer(Map<String, Object> mfa) {
        this.mfaMin = String.valueOf(mfa.get("min"));
        this.mfaMax = String.valueOf(mfa.get("max"));
        this.mfaNoMax = String.valueOf(mfa.get("noMax"));
        this.mfaSuggestionMin = String.valueOf(mfa.get("suggestionMin"));
        this.mfaSuggestionMax = String.valueOf(mfa.get("suggestionMax"));
    }

    /**
     * Number of children
     */
    private String nocMin;
    private String nocMax;
    private String nocNoMax;
    private String nocSuggestionMin;
    private String nocSuggestionMax;

    @JsonProperty("nocThreshold")
    private void nocDeserializer(Map<String, Object> noc) {
        this.nocMin = String.valueOf(noc.get("min"));
        this.nocMax = String.valueOf(noc.get("max"));
        this.nocNoMax = String.valueOf(noc.get("noMax"));
        this.nocSuggestionMin = String.valueOf(noc.get("suggestionMin"));
        this.nocSuggestionMax = String.valueOf(noc.get("suggestionMax"));
    }

    /**
     * Number of public methods
     */
    private String npmMin;
    private String npmMax;
    private String npmNoMax;
    private String npmSuggestionMin;
    private String npmSuggestionMax;

    @JsonProperty("npmThreshold")
    private void npmDeserializer(Map<String, Object> npm) {
        this.npmMin = String.valueOf(npm.get("min"));
        this.npmMax = String.valueOf(npm.get("max"));
        this.npmNoMax = String.valueOf(npm.get("noMax"));
        this.npmSuggestionMin = String.valueOf(npm.get("suggestionMin"));
        this.npmSuggestionMax = String.valueOf(npm.get("suggestionMax"));
    }

    /**
     * Response for a Class
     */
    private String rfcMin;
    private String rfcMax;
    private String rfcNoMax;
    private String rfcSuggestionMin;
    private String rfcSuggestionMax;

    @JsonProperty("rfcThreshold")
    private void rfcDeserializer(Map<String, Object> rfc) {
        this.rfcMin = String.valueOf(rfc.get("min"));
        this.rfcMax = String.valueOf(rfc.get("max"));
        this.rfcNoMax = String.valueOf(rfc.get("noMax"));
        this.rfcSuggestionMin = String.valueOf(rfc.get("suggestionMin"));
        this.rfcSuggestionMax = String.valueOf(rfc.get("suggestionMax"));
    }

    /**
     * Weighted methods per class
     */
    private String wmcMin;
    private String wmcMax;
    private String wmcNoMax;
    private String wmcSuggestionMin;
    private String wmcSuggestionMax;

    @JsonProperty("wmcThreshold")
    private void wmcDeserializer(Map<String, Object> wmc) {
        this.wmcMin = String.valueOf(wmc.get("min"));
        this.wmcMax = String.valueOf(wmc.get("max"));
        this.wmcNoMax = String.valueOf(wmc.get("noMax"));
        this.wmcSuggestionMin = String.valueOf(wmc.get("suggestionMin"));
        this.wmcSuggestionMax = String.valueOf(wmc.get("suggestionMax"));
    }


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