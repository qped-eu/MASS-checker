package eu.qped.java.checkers.mass;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Kind of Coverage Miss
 * <p>
 * Message is applicable if code is fully missed or at least partially missed. For information on partial coverage see [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
 * 
 */
public enum ShowFor {

    FULLY_MISSED("FULLY_MISSED"),
    PARTIALLY_MISSED("PARTIALLY_MISSED");
    private final String value;
    private final static Map<String, ShowFor> CONSTANTS = new HashMap<String, ShowFor>();

    static {
        for (ShowFor c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ShowFor(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static ShowFor fromValue(String value) {
        ShowFor constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}