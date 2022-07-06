package eu.qped.java.checkers.classdesign.enums;

public enum AccessModifier {

    PRIVATE(4, "private"),
    PACKAGE_PRIVATE(3, ""),
    PROTECTED(2, "protected"),
    PUBLIC(1, "public");

    private final int rank;
    private final String name;

    AccessModifier(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public String getKeyword() {
        return name;
    }

}
