package eu.qped.racket;

import eu.qped.racket.interpret.DrRacketInterpreter;
import eu.qped.racket.buildingBlocks.SyntaxChecker;

public class main {
    public static void main(String[] args) {
        try {

            String rktString = "(+ 1 1)";

            DrRacketInterpreter interpreter = new DrRacketInterpreter(rktString);
            System.out.println(interpreter.getXml());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}