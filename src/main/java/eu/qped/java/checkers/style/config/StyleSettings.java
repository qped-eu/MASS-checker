package eu.qped.java.checkers.style.config;

import eu.qped.framework.CheckLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleSettings {
    private int maxClassLength;
    private int maxMethodLength;
    private int maxFieldsCount;
    private int maxCycloComplexity;

    private String varNamesRegEx;
    private String methodNamesRegEx;
    private String classNameRegEx;
    private String methodParameterNamesRegEx;


    private CheckLevel mainLevel;

    private CheckLevel namesLevel;
    private CheckLevel complexityLevel;
    private CheckLevel basisLevel;

    private String language;
}
