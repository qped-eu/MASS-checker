package eu.qped.java.checkers.style;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StyleViolation {
    private  String rule;
    private  String description;
    private  int line;
    private  int priority;

    public StyleViolation(String rule, String description, int line , int priority) {
        this.rule = rule;
        this.description = description;
        this.line = line;
        this.priority = priority;
    }
}
