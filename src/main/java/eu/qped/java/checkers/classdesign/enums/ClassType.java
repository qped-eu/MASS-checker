package eu.qped.java.checkers.classdesign.enums;

public enum ClassType {

    CLASS("class"),
    INTERFACE("interface");

    private final String type;
    ClassType(final String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
