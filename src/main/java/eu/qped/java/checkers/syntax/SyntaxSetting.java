package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxSetting {

    private CheckLevel checkLevel;
    private String language;

}
