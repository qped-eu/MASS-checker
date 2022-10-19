package eu.qped.java.checkers.classdesign.enums;

public enum ClassMemberType {

    FIELD("field"),
    METHOD("method");

    private final String type;
    ClassMemberType(final String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
