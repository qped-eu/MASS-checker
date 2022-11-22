package eu.qped.java.checkers.mass;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Program Format
 * <p>
 * Specify whether the student solution follows plain Java or Maven conventions for locations of base and test classes.
 * 
 */
public enum Convention {

    JAVA("JAVA"),
    MAVEN("MAVEN");
    private final String value;
    private final static Map<String, Convention> CONSTANTS = new HashMap<String, Convention>();

    static {
        for (Convention c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    Convention(String value) {
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
    public static Convention fromValue(String value) {
        Convention constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}