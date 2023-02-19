package eu.qped.framework.feedback.taskspecificfeedback;

import eu.qped.framework.feedback.ConceptReference;
import eu.qped.framework.feedback.hint.Hint;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSpecificFeedback {
    @NonNull
    private String technicalCause;
    @Nullable
    private String readableCause;
    @Nullable
    private List<Hint> hints;
    @Nullable
    private ConceptReference conceptReference;

    public List<Hint> getHints() {
        return hints == null ? Collections.emptyList() : hints.stream().map(Hint::clone).collect(Collectors.toList());
    }
}
