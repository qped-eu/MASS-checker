package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class FieldKeywordConfig extends KeywordConfig {

    private String transientModifier;
    private String volatileModifier;
    private String fieldType;

    public FieldKeywordConfig() {

        transientModifier = KeywordChoice.IGNORE.toString();
        volatileModifier = KeywordChoice.IGNORE.toString();
        fieldType = "";
    }

    public String getTransientModifier() {
        return transientModifier;
    }

    public void setTransientModifier(String transientModifier) {
        this.transientModifier = transientModifier;
    }

    public String getVolatileModifier() {
        return volatileModifier;
    }

    public void setVolatileModifier(String volatileModifier) {
        this.volatileModifier = volatileModifier;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
