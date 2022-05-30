package eu.qped.java.checkers.style;

import eu.qped.framework.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleFeedback extends Feedback {

    private String desc;
    private String line;
    private String example;

    public StyleFeedback( String desc,String body , String example , String line) {
        super(body);
        this.example = example;
        this.line = line;
        this.desc = desc;
    }
}
