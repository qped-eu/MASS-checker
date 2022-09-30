package eu.qped.framework.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConceptReference {

    private String referenceName;
    private String referenceLink;
    private List<Integer> pageNumbers;
    private String section;

}
