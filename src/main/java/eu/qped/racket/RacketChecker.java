package eu.qped.racket;

import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.racket.interpret.DrRacketInterpreter;
import eu.qped.racket.buildingBlocks.SyntaxChecker;

public class RacketChecker implements Checker {
    @Override
    public void check(QfObject qfObject) throws Exception {

        String studentAnswer = qfObject.getAnswer();
        String solution = qfObject.getQuestion().getSolution();

        String evaluatedAnswer = "EMPTY";

        try {
            SyntaxChecker syntaxChecker = new SyntaxChecker();
            evaluatedAnswer = syntaxChecker.check(studentAnswer);

            if (evaluatedAnswer.equals("")) {
                DrRacketInterpreter inter = new DrRacketInterpreter(studentAnswer);
                inter.evaluate();
                evaluatedAnswer = inter.evaluateExpressions();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        String[] feedback = new String[5];

        feedback[0] = "## Your Input was:";
        feedback[1] = "# " + studentAnswer;
        feedback[2] = "## The Output is:";
        feedback[3] = "# " + evaluatedAnswer;
        feedback[4] = "# " + (evaluatedAnswer.compareTo(solution) == 0? "Your Answer is correct!" : "Your Answer is wrong!");

        qfObject.setFeedback(feedback);
    }
}
