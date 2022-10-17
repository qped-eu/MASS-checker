package eu.qped.java.checkers.classdesign.exceptions;

public class NoModifierException extends Exception {

    public NoModifierException() {
        super("At least one modifier has to be allowed.");
    }
}
