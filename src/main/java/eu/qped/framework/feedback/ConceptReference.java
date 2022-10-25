package eu.qped.framework.feedback;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConceptReference {

    @NonNull
    private String referenceName;
    @Nullable
    private String referenceLink;
    @Nullable
    private List<Integer> pageNumbers;
    @Nullable
    private String section;

}
