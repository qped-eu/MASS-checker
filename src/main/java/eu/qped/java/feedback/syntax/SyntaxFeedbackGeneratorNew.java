package eu.qped.java.feedback.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.feedback.FeedbackGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntaxFeedbackGeneratorNew implements FeedbackGenerator<SyntaxFeedbackNew,SyntaxError> {
    private final static String ERROR_TRIGGER_CONS = " Error code: ";
    private final static String LINE_NUMBER_CONS = " Line: ";
    private final static String ERROR_MSG_CONS = " Error type: ";
    private final static String FEEDBACK_CONS = " Feedback: ";
    private final static String NEW_LINE = "\n\n";
    private final static ArrayList<String> TYPES = new ArrayList<>();

    static {
        TYPES.add("for");
        TYPES.add("switch");
        TYPES.add("while");
        TYPES.add("if");
        TYPES.add("else");
        TYPES.add("System");
        TYPES.add("break");
        TYPES.add("continue");
        TYPES.add("case");
    }

    private List<SyntaxFeedbackNew> syntaxFeedbacks;
    private CheckLevel level;
    private String sourceCode;
    private String example;

    @Override
    public List<SyntaxFeedbackNew> generateFeedbacks(List<SyntaxError> errors) {
        List<SyntaxFeedbackNew> result = new ArrayList<>();
        errors.forEach(syntaxError -> {
            // build Feedback body
            List<SyntaxFeedbackNew> relatedSyntaxFeedbacks = this.getFeedback(syntaxError);
            relatedSyntaxFeedbacks = relatedSyntaxFeedbacks.stream().filter(relatedSyntaxFeedback -> {
               return relatedSyntaxFeedback.getErrorMessage().equals(syntaxError.getErrorMsg());
            }).collect(Collectors.toList());
            relatedSyntaxFeedbacks.forEach(relatedSyntaxFeedback -> {
                try {
                    CharSequence source = syntaxError.getSource().getCharContent(false);
                    relatedSyntaxFeedback.setBody(""
                            + "* Compiler error\n\n"
                            + "* About this Error: " + this.getFeedback(syntaxError).get(0).getFeedbackContent() + "\n\n"
                            + "* Cause of error:\n\n"
                                + "------------------------------------------------------------"
                                + "\n\n"
                                + source.subSequence(0 , (int) syntaxError.getStartPos())
                                + "^"
                                + source.subSequence((int) syntaxError.getStartPos(),source.length()) + ""
                                + "\n\n"
                                + "-------------------------------------------"
                                + "\n\n"
                            + "* Example to fix this error: "
                            + relatedSyntaxFeedback.getSolutionExample()
                            + ""
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            result.addAll(relatedSyntaxFeedbacks);
        });
        return result;
    }

    public List<SyntaxFeedbackNew> getFeedback(SyntaxError syntaxError) {
        List<SyntaxFeedbackNew> result = new ArrayList<>();
        SyntaxFeedbackDataNew syntaxFeedbackDataNew = SyntaxFeedbackDataNew.builder().build();
        result = SyntaxFeedbackDataNew.getSyntaxFeedbackByErrorCode().get(syntaxError.getErrorCode());
        result.stream().filter(syntaxFeedback -> {
            return syntaxFeedback.getErrorMessage().equals(syntaxError.getErrorMsg());
        });
        return result;
    }

    public static void main(String[] args) {
        String code = ""
                + "public static void main (String[] args){  "
                    + "int i = 0    "
                + "}"
                + "public static void test () { "
                    + "int g = 0;"
                + "}"
                ;

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().answer(code).build();
//



        Map<String, String> mainSettings = new HashMap<>();
        mainSettings.put("semanticNeeded", "false");
        mainSettings.put("syntaxLevel", "2");
        mainSettings.put("preferredLanguage", "en");
        mainSettings.put("styleNeeded", "false");


        MainSettings mainSettingsConfiguratorConf = new MainSettings(mainSettings);

        MassExecutor massE = new MassExecutor(null, null, syntaxChecker, mainSettingsConfiguratorConf);
        massE.execute();
        List<String> result = new ArrayList<>();
        for (eu.qped.java.feedback.syntaxLagacy.SyntaxFeedback syntax : massE.getSyntaxFeedbacks()) {
            result.add(""
                    + syntax.getFeedbackContent()
            );
        }
        System.out.println(result);
    }
}
