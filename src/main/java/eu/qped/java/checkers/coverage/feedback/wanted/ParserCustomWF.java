package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;

import java.util.Objects;
import java.util.regex.*;



/**
 * Parses a string representing {@link WantedFeedback}
 * @author Herfurth
 * @version 1.0
 */
public class ParserCustomWF {
    /**
     * Defines a group of {@link Matcher#group(int)}
     */
    private static final int CLASS = 1, TYPE = 2, IDENTIFIER = 3, CUSTOM = 4;

    /**
     * Defines the used pattern to parse custom feedback.
     * Example:
     * CLASSNAME:TEST:METHODNAME:MY MSG
     */
    private final Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*):(" + FeedbackType.TEST + "|" + FeedbackType.COVERAGE + "|" + FeedbackType.CUSTOM + "):([a-zA-Z0-9]*):(.*)$");
    private Matcher matcher;
    private boolean notLocked = false;

    protected boolean parse(String string) {
        notLocked = false;
        if (Objects.nonNull(string)) {
            matcher = pattern.matcher(string);
            notLocked = matcher.find();
        }
        return notLocked;
    }

    protected String className() {
        if (notLocked) return matcher.group(CLASS);
        return null;
    }

    protected String identifier() {
        if (notLocked) return matcher.group(IDENTIFIER);
        return null;
    }

    protected FeedbackType type() {
        if (notLocked) return FeedbackType.valueOf(matcher.group(TYPE));
        return null;
    }

    protected String customWF() {
        if (notLocked) return matcher.group(CUSTOM);
        return null;
    }

}
