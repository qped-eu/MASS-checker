package eu.qped.java.checkers.classdesign.enums;

public enum KeywordChoice {

    YES("yes"),
    NO("no"),
    DONTCARE("don't care");

    private final String choice;
    KeywordChoice(final String choice) {
        this.choice = choice;
    }

    public String toString() {
        return this.choice;
    }
}
