
package eu.qped.java.checkers.mass;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Test Coverage Check Configuration
 * <p>
 * Configure the Test Coverage Checker.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "showFullCoverageReport",
    "showTestFailures",
    "feedback"
})

public class QfCoverageSettings {

    /**
     * Show Full Coverage Report
     * <p>
     * Specify whether to show a full report of the test coverage.
     * (Required)
     * 
     */
    @JsonProperty("showFullCoverageReport")
    @JsonPropertyDescription("Specify whether to show a full report of the test coverage.")
    private Boolean showFullCoverageReport = false;
    /**
     * Show Test Failures
     * <p>
     * Specify whether to show a description of the failed tests.
     * (Required)
     * 
     */
    @JsonProperty("showTestFailures")
    @JsonPropertyDescription("Specify whether to show a description of the failed tests.")
    private Boolean showTestFailures = true;
    /**
     * Feedback Configuration
     * <p>
     * Specify the feedback messages per coverage miss.
     * (Required)
     * 
     */
    @JsonProperty("feedback")
    @JsonPropertyDescription("Specify the feedback messages per coverage miss.")
    private List<Feedback> feedback = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    /**
     * Show Full Coverage Report
     * <p>
     * Specify whether to show a full report of the test coverage.
     * (Required)
     * 
     */
    @JsonProperty("showFullCoverageReport")
    public Boolean getShowFullCoverageReport() {
        return showFullCoverageReport;
    }

    /**
     * Show Full Coverage Report
     * <p>
     * Specify whether to show a full report of the test coverage.
     * (Required)
     * 
     */
    @JsonProperty("showFullCoverageReport")
    public void setShowFullCoverageReport(Boolean showFullCoverageReport) {
        this.showFullCoverageReport = showFullCoverageReport;
    }

    /**
     * Show Test Failures
     * <p>
     * Specify whether to show a description of the failed tests.
     * (Required)
     * 
     */
    @JsonProperty("showTestFailures")
    public Boolean getShowTestFailures() {
        return showTestFailures;
    }

    /**
     * Show Test Failures
     * <p>
     * Specify whether to show a description of the failed tests.
     * (Required)
     * 
     */
    @JsonProperty("showTestFailures")
    public void setShowTestFailures(Boolean showTestFailures) {
        this.showTestFailures = showTestFailures;
    }

    /**
     * Feedback Configuration
     * <p>
     * Specify the feedback messages per coverage miss.
     * (Required)
     * 
     */
    @JsonProperty("feedback")
    public List<Feedback> getFeedback() {
        return feedback;
    }

    /**
     * Feedback Configuration
     * <p>
     * Specify the feedback messages per coverage miss.
     * (Required)
     * 
     */
    @JsonProperty("feedback")
    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(QfCoverageSettings.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("showFullCoverageReport");
        sb.append('=');
        sb.append(((this.showFullCoverageReport == null)?"<null>":this.showFullCoverageReport));
        sb.append(',');
        sb.append("showTestFailures");
        sb.append('=');
        sb.append(((this.showTestFailures == null)?"<null>":this.showTestFailures));
        sb.append(',');
        sb.append("feedback");
        sb.append('=');
        sb.append(((this.feedback == null)?"<null>":this.feedback));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalProperties == null) ? 0 : additionalProperties.hashCode());
		result = prime * result + ((feedback == null) ? 0 : feedback.hashCode());
		result = prime * result + ((showFullCoverageReport == null) ? 0 : showFullCoverageReport.hashCode());
		result = prime * result + ((showTestFailures == null) ? 0 : showTestFailures.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QfCoverageSettings other = (QfCoverageSettings) obj;
		if (additionalProperties == null) {
			if (other.additionalProperties != null)
				return false;
		} else if (!additionalProperties.equals(other.additionalProperties))
			return false;
		if (feedback == null) {
			if (other.feedback != null)
				return false;
		} else if (!feedback.equals(other.feedback))
			return false;
		if (showFullCoverageReport == null) {
			if (other.showFullCoverageReport != null)
				return false;
		} else if (!showFullCoverageReport.equals(other.showFullCoverageReport))
			return false;
		if (showTestFailures == null) {
			if (other.showTestFailures != null)
				return false;
		} else if (!showTestFailures.equals(other.showTestFailures))
			return false;
		return true;
	}


}
