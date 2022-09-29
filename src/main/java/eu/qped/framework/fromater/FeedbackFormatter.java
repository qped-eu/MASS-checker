package eu.qped.framework.fromater;

import eu.qped.framework.feedback.Feedback;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.emphasis.StrikeThroughText;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackFormatter {


    public static void example() throws Exception {
        StringBuilder sb = new StringBuilder()
                .append(new Text("I am normal")).append("\n")
                .append(new BoldText("I am bold")).append("\n")
                .append(new ItalicText("I am italic")).append("\n")
                .append(new StrikeThroughText("I am strike-through"));

        System.out.println(sb);
    }


    public static void main(String[] args) throws Exception {
        example();
    }

    public List<String> getFormattedFeedbacks(List<Feedback> feedbacks) {


//        var feedbackTitle = feedbacks.get(0).getTitle();


//        String exampleTemplate = "%s example";
        String hintTemplate = "%s severity";
        String severityTemplate = "%s severity";
        String titleTemplate = "%s title";

        String template = "";

//        if (feedbackTitle == null || "".equals(feedbackTitle)) {
            // code
//        }

//        String.format(template, feedbacks.get(0).getTitle());


        return feedbacks.stream().map(feedback -> {
            return new StringBuilder()
//                    .append(feedback.getTitle())
                    .append(feedback.getSeverity())
                    .append(feedback.getHints())
                    .append(feedback.getErrorLocation()).toString();
        }).collect(Collectors.toList());
    }
}
