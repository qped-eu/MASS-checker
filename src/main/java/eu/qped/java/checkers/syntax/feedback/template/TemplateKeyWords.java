package eu.qped.java.checkers.syntax.feedback.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateKeyWords {

    private String titleSyntax;
    private String titleStyle;
    private String titleSemantic;
    private String moreInformation;
    private String at;
    private String page;

}
