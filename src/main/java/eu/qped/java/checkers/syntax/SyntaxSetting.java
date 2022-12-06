package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.checkerabstract.AbstractSetting;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SyntaxSetting extends AbstractSetting {

    private CheckLevel checkLevel;


}
