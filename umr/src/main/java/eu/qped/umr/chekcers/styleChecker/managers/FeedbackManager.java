package eu.qped.umr.chekcers.styleChecker.managers;

import eu.qped.umr.chekcers.styleChecker.StyleFeedbackGenerator;
import eu.qped.umr.model.StyleFeedback;
import eu.qped.umr.model.StyleViolation;

import java.util.ArrayList;

public class FeedbackManager {


    private  final ArrayList<StyleViolation> violations;

    private final  ArrayList<StyleFeedback> styleFeedbacks = new ArrayList<>();

    private FeedbackManager(ArrayList<StyleViolation> violations){
        this.violations = violations;
    }

    public static FeedbackManager createFeedbackManager(ArrayList<StyleViolation> styleViolations) {
        return new FeedbackManager(styleViolations);
    }


    public void buildFeedback(){
        StyleFeedbackGenerator styleFeedbackGenerator = StyleFeedbackGenerator.createStyleFeedbackGenerator();
        for (StyleViolation styleViolation : this.violations){
            styleFeedbacks.add(new StyleFeedback(styleViolation.getDescription(),
                    styleFeedbackGenerator.getFeedbackBody(styleViolation.getRule()),
                    styleFeedbackGenerator.getFeedbackExample(styleViolation.getRule()),
                    "at Line: " + styleViolation.getLine()));
        }
    }
    public ArrayList<StyleFeedback> getStyleFeedbacks() {
        return styleFeedbacks;
    }

    public ArrayList<StyleViolation> getViolations() {
        return violations;
    }
}
