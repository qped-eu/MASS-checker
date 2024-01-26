
package eu.qped.java.checkers.mass;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


/**
 * Test Mutation Check Configuration
 * <p>
 * Configure the Mutation Test Checker.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "showFullMutationReport"
})

@Getter
@Setter
public class QfMutationSettings {

    /**
     * Show Full Mutation Report
     * <p>
     * Specify whether to show a full report of the mutation tests.
     * (Required)
     *
     */
    @JsonProperty("showFullMutationReport")
    @JsonPropertyDescription("Specify whether to show a full report of the mutation tests.")
    private Boolean showFullMutationReport = false;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    /**
     * Show Full Mutation Test Report
     * <p>
     * Specify whether to show a full report of the mutation tests.
     * (Required)
     *
     */
    @JsonProperty("ShowFullMutationReport")
    public Boolean getShowFullMutationReport() {
        return showFullMutationReport;
    }

    /**
     * Show Full Mutation Test Report
     * <p>
     * Specify whether to show a full report of the mutation tests.
     * (Required)
     *
     */
    @JsonProperty("ShowFullMutationReport")
    public void setShowFullMutationReport(Boolean showFullMutationReport) {
        this.showFullMutationReport = showFullMutationReport;
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
        sb.append(QfMutationSettings.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("showFullMutationReport");
        sb.append('=');
        sb.append(((this.showFullMutationReport == null)?"<null>":this.showFullMutationReport));
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
        result = prime * result + ((showFullMutationReport == null) ? 0 : showFullMutationReport.hashCode());
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
        QfMutationSettings other = (QfMutationSettings) obj;
        if (additionalProperties == null) {
            if (other.additionalProperties != null)
                return false;
        } else if (!additionalProperties.equals(other.additionalProperties))
            return false;
        if (showFullMutationReport == null) {
            if (other.showFullMutationReport != null)
                return false;
        } else if (!showFullMutationReport.equals(other.showFullMutationReport))
            return false;
        return true;
    }


}
