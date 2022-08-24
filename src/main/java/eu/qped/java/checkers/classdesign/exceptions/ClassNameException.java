package eu.qped.java.checkers.classdesign.exceptions;

public class ClassNameException extends Exception {

    public ClassNameException() {
        super("Class name is required to be able to match classes.");
    }
}
