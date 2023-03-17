package eu.qped.racket;

import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;

public class RacketChecker implements Checker {
    @Override
    public void check(QfObject qfObject) throws Exception {
        String answer = qfObject.getAnswer();

        String[] feedback = new String[1];

        feedback[0] = "Hallo Welt";

        qfObject.setFeedback(feedback);
    }
}
