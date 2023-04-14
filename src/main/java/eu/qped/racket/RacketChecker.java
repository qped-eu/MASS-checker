package eu.qped.racket;

import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.racket.interpret.DrRacketInterpreter;
import eu.qped.racket.test.SyntaxChecker;

public class RacketChecker implements Checker {
    @Override
    public void check(QfObject qfObject) throws Exception {
        String answer = qfObject.getAnswer();
        String solution = qfObject.getCheckerClass();

        String message = "EMPTY";

        try {
            SyntaxChecker syntaxChecker = new SyntaxChecker();
            message = syntaxChecker.check(answer);

            if (message.equals("")) {
                DrRacketInterpreter inter = new DrRacketInterpreter(answer);
                inter.master();
                message = inter.evaluateExpressions();
            }

        } catch (Exception e) {
            System.out.println(e);
            message = e.getMessage();
        }


        String[] feedback = new String[4];

        feedback[0] = "## Your Input was:";
        feedback[1] = "# " + answer;
        feedback[2] = "## The Output is:";
        feedback[3] = "# " + message;

        qfObject.setFeedback(feedback);
    }
}
