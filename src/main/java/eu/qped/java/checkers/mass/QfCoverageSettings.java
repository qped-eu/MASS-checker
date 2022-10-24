
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
    "privateImplementation",
    "showExtraMisses",
    "feedback"
})

public class QfCoverageSettings {

    /**
     * Private Base Implementation
     * <p>
     * Specify the URL of the ZIP-file containing the private implementation.
     * 
     */
    @JsonProperty("privateImplementation")
    @JsonPropertyDescription("Specify the URL of the ZIP-file containing the private implementation.")
    private String privateImplementation;
    /**
     * Show Extra Misses
     * <p>
     * Specify whether to show missed or partially missed lines for which no feedback message is configured.
     * (Required)
     * 
     */
    @JsonProperty("showExtraMisses")
    @JsonPropertyDescription("Specify whether to show missed or partially missed lines for which no feedback message is configured.")
    private Boolean showExtraMisses = false;
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
     * Private Base Implementation
     * <p>
     * Specify the URL of the ZIP-file containing the private implementation.
     * 
     */
    @JsonProperty("privateImplementation")
    public String getPrivateImplementation() {
        return privateImplementation;
    }

    /**
     * Private Base Implementation
     * <p>
     * Specify the URL of the ZIP-file containing the private implementation.
     * 
     */
    @JsonProperty("privateImplementation")
    public void setPrivateImplementation(String privateImplementation) {
        this.privateImplementation = privateImplementation;
    }

    /**
     * Show Extra Misses
     * <p>
     * Specify whether to show missed or partially missed lines for which no feedback message is configured.
     * (Required)
     * 
     */
    @JsonProperty("showExtraMisses")
    public Boolean getShowExtraMisses() {
        return showExtraMisses;
    }

    /**
     * Show Extra Misses
     * <p>
     * Specify whether to show missed or partially missed lines for which no feedback message is configured.
     * (Required)
     * 
     */
    @JsonProperty("showExtraMisses")
    public void setShowExtraMisses(Boolean showExtraMisses) {
        this.showExtraMisses = showExtraMisses;
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
        sb.append("privateImplementation");
        sb.append('=');
        sb.append(((this.privateImplementation == null)?"<null>":this.privateImplementation));
        sb.append(',');
        sb.append("showExtraMisses");
        sb.append('=');
        sb.append(((this.showExtraMisses == null)?"<null>":this.showExtraMisses));
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
        int result = 1;
        result = ((result* 31)+((this.feedback == null)? 0 :this.feedback.hashCode()));
        result = ((result* 31)+((this.showExtraMisses == null)? 0 :this.showExtraMisses.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.privateImplementation == null)? 0 :this.privateImplementation.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof QfCoverageSettings) == false) {
            return false;
        }
        QfCoverageSettings rhs = ((QfCoverageSettings) other);
        return (((((this.feedback == rhs.feedback)||((this.feedback!= null)&&this.feedback.equals(rhs.feedback)))&&((this.showExtraMisses == rhs.showExtraMisses)||((this.showExtraMisses!= null)&&this.showExtraMisses.equals(rhs.showExtraMisses))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.privateImplementation == rhs.privateImplementation)||((this.privateImplementation!= null)&&this.privateImplementation.equals(rhs.privateImplementation))));
    }

}
