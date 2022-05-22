package eu.qped.java.feedback.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
public class SyntaxFeedbackGeneratorNew extends AbstractFeedbackGenerator {

    private final AtomicInteger feedbackCounter = new AtomicInteger(0);

    @Override
    protected String generateHeader() {
        return "Error " + String.format("%02d", feedbackCounter.incrementAndGet()) + ":";
    }

    @Override
    protected String generateFeedbackMessage(SyntaxError error) {
        String result = "";
        SyntaxFeedbackMessages syntaxFeedbackMessages = SyntaxFeedbackMessages.builder().build();
        if (syntaxFeedbackMessages.getFeedbackMessagesByErrorCode().containsKey(error.getErrorCode())) {
            result += syntaxFeedbackMessages.getFeedbackMessagesByErrorCode().get(error.getErrorCode());
        }
        if (syntaxFeedbackMessages.getFeedbackMessagesByErrorMessage().containsKey(error.getErrorMessage())) {
            result += syntaxFeedbackMessages.getFeedbackMessagesByErrorMessage().get(error.getErrorMessage());
        }
        if (result.equals("")) {
            result = error.getErrorMessage();
        }
        return result;
    }

    @Override
    protected String generateErrorLine(SyntaxError syntaxError) {
        return "At line :" + String.format("%d", syntaxError.getLine());
    }

    @Override
    protected String generateErrorSource(SyntaxError syntaxError) {
        return syntaxError.getErrorSourceCode();
    }

    @Override
    protected String generateSolutionExample(SyntaxError error) {
        String result = "";
        SyntaxFeedbackSolutionExamples syntaxFeedbackSolutionExamples = SyntaxFeedbackSolutionExamples.builder().build();
        if (syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorCode().containsKey(error.getErrorCode())) {
            result += syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorCode().get(error.getErrorCode());
        }
        if (syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorMessage().containsKey(error.getErrorMessage())) {
            result += syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorMessage().get(error.getErrorMessage());
        }
        return result;
    }


    public static void main(String[] args) {
        String code = ""
                + "public static void main (String[] args) { \n"
                + " int i = 0; b = 0;  \n"
                + "  \n"
                + "} \n";

        System.out.println("ABC");
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).level(CheckLevel.ADVANCED).build();
        Map<String, String> mainSettings = new HashMap<>();
        mainSettings.put("semanticNeeded", "false");
        mainSettings.put("syntaxLevel", "2");
        mainSettings.put("preferredLanguage", "en");
        mainSettings.put("styleNeeded", "false");

        MainSettings mainSettingsConfiguratorConf = new MainSettings(mainSettings);

        MassExecutor massE = new MassExecutor(null, null, syntaxChecker, mainSettingsConfiguratorConf);
        massE.execute();

        List<String> result = new ArrayList<>();


//        for (SyntaxFeedback syntax : massE.getSyntaxFeedbacks()) {
//            String s = ""
//                    + syntax.getFeedbackContent()
//                    + "\n\n"
//                    + "--------------------------------------------------";
//            System.out.println(s);
//
//        }


//        for (SyntaxFeedbackNew syntax : massE.getSyntaxFeedbackNews()) {
//            String s = ""
//                    + syntax.toString()
//                    + "\n\n"
//                    + "--------------------------------------------------";
//            System.out.println(s);
//
//        }
    }


}
