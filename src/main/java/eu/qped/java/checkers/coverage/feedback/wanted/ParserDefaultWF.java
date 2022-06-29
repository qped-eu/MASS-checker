package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parses a string representing {@link WantedFeedback}.
 * This wantedFeedback is used to set the wantedFeedback for one or all classes.
 * @author Herfurth
 * @version 1.0
 */
public class ParserDefaultWF {
    /**
     * Defines a group of {@link Matcher#group(int)}
     */
    private static final int CLASS = 1, TYPE = 2;

    /**
     * Defines the used pattern to parse default feedback.
     * Example:
     * CLASSNAME:TEST
     */
    private final Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*):(" + FeedbackType.TEST + "|" + FeedbackType.COVERAGE + ")$");
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

    protected FeedbackType type() {
        if (notLocked) return FeedbackType.valueOf(matcher.group(TYPE));
        return null;
    }
}
