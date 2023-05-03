package eu.qped.framework.feedback.defaultfeedback;

import eu.qped.framework.feedback.hint.Hint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredFeedback {
    private String technicalCause;
    private String readableCause;
    private List<Hint> hints;


    public List<Hint> getHints() {
        return hints == null ? Collections.emptyList() : hints.stream().map(Hint::clone).collect(Collectors.toList());
    }


}
