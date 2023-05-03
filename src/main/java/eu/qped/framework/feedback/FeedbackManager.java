package eu.qped.framework.feedback;

import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackManager {
    private List<Feedback> feedbacks;
    private boolean isFormatted = false;

    public List<Feedback> addFeedback(Feedback feedback) {
        if (feedbacks == null) feedbacks = Collections.emptyList();
        feedback.setFormatted(false);
        isFormatted = false;
        feedbacks.add(feedback);
        return feedbacks;
    }

    public List<Feedback> formatFeedbacks() {
        if (feedbacks == null) feedbacks = Collections.emptyList();
        feedbacks = feedbacks.stream()
                .map(feedback -> {
                    if (feedback.isFormatted()) {
                        return feedback;
                    } else {
                        feedback.setFormatted(true);
                        return feedback.format();
                    }
                })
                .collect(Collectors.toList());
        ;
        isFormatted = true;
        return feedbacks;
    }

    public List<String> buildFeedbackInTemplate(@NonNull String language) {
        if (feedbacks == null) feedbacks = Collections.emptyList();
        if (!isFormatted) feedbacks = formatFeedbacks();
        return feedbacks.stream()
                .map(feedback -> feedback.buildFeedbackInTemplate(language))
                .collect(Collectors.toList())
                ;
    }

    public List<Feedback> filter(Predicate<Feedback> filter) {
        return feedbacks.stream().filter(filter).collect(Collectors.toList());
    }

    public <K> Map<K, List<Feedback>> groupBy(Function<Feedback, K> groupByFunction) {
        return feedbacks.stream().collect(Collectors.groupingBy(groupByFunction, Collectors.toList()));
    }

    public Map<String, List<Feedback>> groupByFileName() {
        return groupBy(feedback -> (feedback.getRelatedLocation() != null && !feedback.getRelatedLocation().getFileName().isEmpty()) ?
                feedback.getRelatedLocation().getFileName()
                : "");
    }


}