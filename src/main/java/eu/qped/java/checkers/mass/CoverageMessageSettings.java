package eu.qped.java.checkers.mass;



import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Feedback Message
 * <p>
 * Specify the location of the coverage miss
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ID",
    "showFor",
    "fileName",
    "lineRanges",
    "message",
    "suppresses"
})

public class CoverageMessageSettings {

    /**
     * Message ID
     * <p>
     * Optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     * regexp = "[_a-zA-Z0-9]+"
     * 
     */
    @JsonProperty("ID")
    @JsonPropertyDescription("Optionally specify a unique name to this feedback message to define constraints between messages.")
    private String id;
    /**
     * Kind of Coverage Miss
     * <p>
     * Specify whether to show the message when a line in the range is not covered at all, partially covered or in both cases. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
     * (Required)
     * 
     */
    @JsonProperty("showFor")
    @JsonPropertyDescription("Specify whether to show the message when a line in the range is not covered at all, partially covered or in both cases. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).")
    private ShowFor showFor = ShowFor.fromValue("BOTH");
    /**
     * File Name
     * <p>
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java)  and the path must correspond to the package of the contained class.
     * (Required)
     * 
     * regexp = "\\s*[_$a-zA-Z0-9]+(/[_$a-zA-Z0-9]+)*.[a-zA-Z0-9]*"
     * 
     */
    @JsonProperty("fileName")
    @JsonPropertyDescription("Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java)  and the path must correspond to the package of the contained class.")
    private String fileName;
    /**
     * Line Ranges
     * <p>
     * Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.
     * (Required)
     * 
     * regexp = "\\s*\\d+(\\s*-\\s*\\d+)?\\s*(,\\s*\\d+(\\s*-\\s*\\d+)?\\s*)*"
     * 
     */
    @JsonProperty("lineRanges")
    @JsonPropertyDescription("Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.")
    private String lineRanges;
    /**
     * Message
     * <p>
     * Specify the message to show when one of the specified lines is missed as configured.
     * (Required)
     * 
     */
    @JsonProperty("message")
    @JsonPropertyDescription("Specify the message to show when one of the specified lines is missed as configured.")
    private String message;
    /**
     * Suppressed Messages
     * <p>
     * Optionally specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
     * 
     * regexp = "[_$a-zA-Z0-9]+\\s*(,\\s*[_$a-zA-Z0-9]+\\s*)*"
     */
    @JsonProperty("suppresses")
    @JsonPropertyDescription("Optionally specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.")
    private String suppresses;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Message ID
     * <p>
     * Optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     */
    @JsonProperty("ID")
    public String getId() {
        return id;
    }

    /**
     * Message ID
     * <p>
     * Optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     */
    @JsonProperty("ID")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Kind of Coverage Miss
     * <p>
     * Specify whether to show the message when a line in the range is not covered at all, partially covered or in both cases. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
     * (Required)
     * 
     */
    @JsonProperty("showFor")
    public ShowFor getShowFor() {
        return showFor;
    }

    /**
     * Kind of Coverage Miss
     * <p>
     * Specify whether to show the message when a line in the range is not covered at all, partially covered or in both cases. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
     * (Required)
     * 
     */
    @JsonProperty("showFor")
    public void setShowFor(ShowFor showFor) {
        this.showFor = showFor;
    }

    /**
     * File Name
     * <p>
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java)  and the path must correspond to the package of the contained class.
     * (Required)
     * 
     */
    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    /**
     * File Name
     * <p>
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java)  and the path must correspond to the package of the contained class.
     * (Required)
     * 
     */
    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Line Ranges
     * <p>
     * Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.
     * (Required)
     * 
     */
    @JsonProperty("lineRanges")
    public String getLineRanges() {
        return lineRanges;
    }

    /**
     * Line Ranges
     * <p>
     * Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.
     * (Required)
     * 
     */
    @JsonProperty("lineRanges")
    public void setLineRanges(String lineRanges) {
        this.lineRanges = lineRanges;
    }

    /**
     * Message
     * <p>
     * Specify the message to show when one of the specified lines is missed as configured.
     * (Required)
     * 
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Message
     * <p>
     * Specify the message to show when one of the specified lines is missed as configured.
     * (Required)
     * 
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Suppressed Messages
     * <p>
     * Optionally specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
     * 
     */
    @JsonProperty("suppresses")
    public String getSuppresses() {
        return suppresses;
    }

    /**
     * Suppressed Messages
     * <p>
     * Optionally specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
     * 
     */
    @JsonProperty("suppresses")
    public void setSuppresses(String suppresses) {
        this.suppresses = suppresses;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
