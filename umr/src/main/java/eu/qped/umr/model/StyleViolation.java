package eu.qped.umr.model;

public class StyleViolation {
    private final String rule;
    private final String description;
    private final int line;
    private final int priority;

    public StyleViolation(String rule, String description, int line , int priority) {
        this.rule = rule;
        this.description = description;
        this.line = line;
        this.priority = priority;
    }

    public String getRule() {
        return rule;
    }

    public String getDescription() {
        return description;
    }

    public int getLine() {
        return line;
    }

    public int getPriority() {
        return priority;
    }
}
