
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
 * Feedback Messages
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
public class Feedback {

    /**
     * Message ID
     * <p>
     * You can optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     */
    @JsonProperty("ID")
    @JsonPropertyDescription("You can optionally specify a unique name to this feedback message to define constraints between messages.")
    private String id;
    /**
     * Kind of Coverage Miss
     * <p>
     * Specify whether to show the message only when all lines in the range are completely missed or they are just partially missed. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
     * (Required)
     * 
     */
    @JsonProperty("showFor")
    @JsonPropertyDescription("Specify whether to show the message only when all lines in the range are completely missed or they are just partially missed. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).")
    private ShowFor showFor = ShowFor.fromValue("PARTIALLY_MISSED");
    /**
     * File Name
     * <p>
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java) and the path must correspond to the package of the contained class.
     * (Required)
     * 
     */
    @JsonProperty("fileName")
    @JsonPropertyDescription("Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java) and the path must correspond to the package of the contained class.")
    private String fileName;
    /**
     * Line Ranges
     * <p>
     * Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.
     * (Required)
     * 
     */
    @JsonProperty("lineRanges")
    @JsonPropertyDescription("Specify relevant missed or partially missed lines as a comma separated list of line numbers or ranges (e.g., 1-3). Line numbers start at 1.")
    private String lineRanges;
    /**
     * Message
     * <p>
     * Specify the message to show when miss as configured occurs.
     * (Required)
     * 
     */
    @JsonProperty("message")
    @JsonPropertyDescription("Specify the message to show when miss as configured occurs.")
    private String message;
    /**
     * Suppressed Messages
     * <p>
     * Specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
     * 
     */
    @JsonProperty("suppresses")
    @JsonPropertyDescription("Specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.")
    private String suppresses;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Message ID
     * <p>
     * You can optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     */
    @JsonProperty("ID")
    public String getId() {
        return id;
    }

    /**
     * Message ID
     * <p>
     * You can optionally specify a unique name to this feedback message to define constraints between messages.
     * 
     */
    @JsonProperty("ID")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Kind of Coverage Miss
     * <p>
     * Specify whether to show the message only when all lines in the range are completely missed or they are at least partially missed. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
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
     * Specify whether to show the message only when all lines in the range are completely missed or they are just partially missed. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
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
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java) and the path must correspond to the package of the contained class.
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
     * Specify the source file containing the lines you refer to. The file must use / as path separator, include the extension (usually .java) and the path must correspond to the package of the contained class.
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
     * Specify the message to show when miss as configured occurs.
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
     * Specify the message to show when miss as configured occurs.
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
     * Specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
     * 
     */
    @JsonProperty("suppresses")
    public String getSuppresses() {
        return suppresses;
    }

    /**
     * Suppressed Messages
     * <p>
     * Specify which feedback messages should be held back when this message is applicable. Specify a comma separated list of the Message IDs of those feedback messages.
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Feedback.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("showFor");
        sb.append('=');
        sb.append(((this.showFor == null)?"<null>":this.showFor));
        sb.append(',');
        sb.append("fileName");
        sb.append('=');
        sb.append(((this.fileName == null)?"<null>":this.fileName));
        sb.append(',');
        sb.append("lineRanges");
        sb.append('=');
        sb.append(((this.lineRanges == null)?"<null>":this.lineRanges));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        sb.append("suppresses");
        sb.append('=');
        sb.append(((this.suppresses == null)?"<null>":this.suppresses));
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
        result = ((result* 31)+((this.lineRanges == null)? 0 :this.lineRanges.hashCode()));
        result = ((result* 31)+((this.fileName == null)? 0 :this.fileName.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.showFor == null)? 0 :this.showFor.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.message == null)? 0 :this.message.hashCode()));
        result = ((result* 31)+((this.suppresses == null)? 0 :this.suppresses.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Feedback) == false) {
            return false;
        }
        Feedback rhs = ((Feedback) other);
        return ((((((((this.lineRanges == rhs.lineRanges)||((this.lineRanges!= null)&&this.lineRanges.equals(rhs.lineRanges)))&&((this.fileName == rhs.fileName)||((this.fileName!= null)&&this.fileName.equals(rhs.fileName))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.showFor == rhs.showFor)||((this.showFor!= null)&&this.showFor.equals(rhs.showFor))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.message == rhs.message)||((this.message!= null)&&this.message.equals(rhs.message))))&&((this.suppresses == rhs.suppresses)||((this.suppresses!= null)&&this.suppresses.equals(rhs.suppresses))));
    }

}
