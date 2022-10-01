package eu.qped.framework.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConceptReference {

    private String referenceName;
    @Nullable
    private String referenceLink;
    @Nullable
    private List<Integer> pageNumbers;
    @Nullable
    private String section;

}
