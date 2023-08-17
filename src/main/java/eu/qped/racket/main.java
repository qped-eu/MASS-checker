package eu.qped.racket;

import eu.qped.racket.interpret.DrRacketInterpreter;
import eu.qped.racket.buildingBlocks.SyntaxChecker;

public class main {
    public static void main(String[] args) {
        try {

            String rktString = "(boolean=? #f #f)";
            //String rktString = "(number? 1)";
            //String rktString = "(equal? 6 \"half dozen\")";
            //String rktString = "(= abc \"8\")";


            DrRacketInterpreter interpreter = new DrRacketInterpreter(rktString);
            System.out.println(interpreter.getXml());

            interpreter.evaluate();
            System.out.println(interpreter.evaluateExpressions());

            double d = Math.sqrt(-16);
            System.out.println(d);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
