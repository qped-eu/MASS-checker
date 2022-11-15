package eu.qped.java.checkers.solutionapproach.configs;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.checkerabstract.AbstractSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SolutionApproachGeneralSettings extends AbstractSetting {
    private CheckLevel checkLevel;

}
