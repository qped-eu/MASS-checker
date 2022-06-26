package eu.qped.java.checkers.classdesign.enums;

public enum KeywordType {

    EMPTY(""),
    OPTIONAL("*");

    private final String type;
    KeywordType(final String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
