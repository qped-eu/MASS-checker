package eu.qped.framework.feedback.defaultfeedback;

import eu.qped.framework.feedback.hint.Hint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredFeedback {
    private String technicalCause;
    private String readableCause;
    private List<Hint> hints;


}
