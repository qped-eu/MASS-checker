package eu.qped.java.checkers.solutionapproach.configs;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.checkerabstract.AbstractSetting;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachGeneralSettings extends AbstractSetting {
    private CheckLevel checkLevel;
}
