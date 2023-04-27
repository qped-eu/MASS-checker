package eu.qped.racket;

import eu.qped.racket.test.SyntaxChecker;

public class main {
    public static void main(String[] args) {
        SyntaxChecker syntaxChecker = new SyntaxChecker();
        System.out.println(syntaxChecker.bracketCheck("(][)")[1]);
    }
}
