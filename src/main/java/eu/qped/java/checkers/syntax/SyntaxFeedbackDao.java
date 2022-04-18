package eu.qped.java.checkers.syntax;

import eu.qped.java.checkers.mass.FeedbackDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
public class SyntaxFeedbackDao implements FeedbackDao<SyntaxFeedback> {

    @Override
    public List<SyntaxFeedback> fetchPotentialFeedbacks(String errorCode) {
        SyntaxFeedbackData repository = SyntaxFeedbackData.builder().build();
        return repository.getDataDocument().get(errorCode);
    }
}
