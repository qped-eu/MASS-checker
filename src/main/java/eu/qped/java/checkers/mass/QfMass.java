package eu.qped.java.checkers.mass;

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

}
