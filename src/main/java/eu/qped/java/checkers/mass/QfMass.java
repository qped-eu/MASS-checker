package eu.qped.java.checkers.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QfMass extends QfObjectBase {

    private QfCoverageSettings coverage;

    private QfSemanticSettings semantic;

    private QfStyleSettings style;

    private QfMetricsSettings metrics;

    private QfSyntaxSettings syntax;

    private QfClassSettings classes;

    private boolean styleSelected;
    private boolean semanticSelected;
    private boolean metricsSelected;
    private boolean coverageSelected;
    private boolean classSelected;
    
    /**
     * Private Base Implementation
     * <p>
     * Specify the URL of the ZIP-file containing the private implementation.
     * 
     */
    @JsonProperty("instructorResources")
    @JsonPropertyDescription("Specify the URL of a ZIP-file containing instructor resources that should be added to the root directory of student resources.")
    private String instructorResources;

}
