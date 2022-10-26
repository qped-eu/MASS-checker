package eu.qped.framework.feedback.defaultjsonfeedback;

import eu.qped.java.checkers.syntax.SyntaxChecker;

public enum CheckerName {

    SyntaxChecker {
        @Override public String toString() { return eu.qped.java.checkers.syntax.SyntaxChecker.class.getSimpleName();}
    },
    White {
        @Override public String toString() { return "light";}
    }
}
