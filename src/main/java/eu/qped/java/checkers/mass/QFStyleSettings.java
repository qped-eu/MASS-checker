package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QFStyleSettings extends QfObjectBase {

    private String mainLevel;

    private String basisLevel;
    private String compLevel;
    private String namesLevel;

    private String classLength;
    private String methodLength;
    private String cycloComplexity;
    private String fieldsCount;

    private String varName;
    private String methodName;
    private String className;
}
