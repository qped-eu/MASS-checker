package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxSetting {

    private CheckLevel checkLevel;
    private String language;


}
