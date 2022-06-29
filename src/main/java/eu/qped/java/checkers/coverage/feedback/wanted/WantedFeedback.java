package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import eu.qped.java.checkers.coverage.feedback.DefaultFB;
import eu.qped.java.checkers.coverage.feedback.StmtFB;
import eu.qped.java.checkers.coverage.feedback.TestFB;

import java.util.Objects;


/**
 * Stores with feedback is wanted by different {@link FeedbackType}
 * @author Herfurth
 * @version 1.0
 */
public class WantedFeedback {

    /**
     * Defines a string that gets replaced in the feedback by a value.
     * These strings are used by all {@link FeedbackType}
     */
    protected static String REPLACE_CLASS = "CLASS", REPLACE_METHOD = "METHOD";
    /**
     * These strings are only used for feedback of type {@link FeedbackType#COVERAGE} or {@link FeedbackType#CUSTOM}
     */
    protected static String REPLACE_INDEX = "INDEX", REPLACE_TYPE = "TYPE";
    /**
     * These strings are only used for feedback of type {@link FeedbackType#TEST} or {@link FeedbackType#CUSTOM}
     */
    protected static String REPLACE_WANT = "WANT", REPLACE_GOT = "GOT";

    private DefaultFB defaultFeedback;
    public final FeedbackType type;
    public String feedback;

    public WantedFeedback(DefaultFB defaultFeedback, FeedbackType type, String feedback) {
        this.defaultFeedback = defaultFeedback;
        this.type = type;
        this.feedback = feedback;
    }

    protected WantedFeedback(FeedbackType type, String feedback) {
        this(new DefaultFB(), type, feedback);
    }

    void setDefaultFeedback(DefaultFB defaultFeedback) {
        this.defaultFeedback = defaultFeedback;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WantedFeedback that = (WantedFeedback) o;
        return type == that.type && Objects.equals(feedback, that.feedback);
    }

    public void setFeedbackBody(TestFB test) {
        if (this.type.equals(FeedbackType.TEST)) {
            feedback = defaultFeedback.testFB();
        }
        replaceTest(test);
    }

    private void replaceCoverage(StmtFB fb) {
        fb.setBody(
                feedback.replace(REPLACE_CLASS, fb.className())
                        .replace(REPLACE_METHOD, fb.methodName())
                        .replace(REPLACE_TYPE, fb.type().name())
                        .replace(REPLACE_INDEX, fb.start() + ""));
    }

    private void replaceTest(TestFB fb) {
        fb.setBody(
                feedback.replace(REPLACE_CLASS, fb.className())
                        .replace(REPLACE_METHOD, fb.methodName())
                        .replace(REPLACE_WANT, fb.want())
                        .replace(REPLACE_GOT, fb.got()));
    }


    public void setFeedbackBody(StmtFB statement) {
        if (type.equals(FeedbackType.COVERAGE) && Objects.nonNull(defaultFeedback)) {
            switch (statement.type()) {
                case IF:
                    feedback = defaultFeedback.ifFB();
                    break;

                case ELSE:
                    feedback = defaultFeedback.elseFB();
                    break;

                case ELSE_IF:
                    feedback = defaultFeedback.elseIfFB();
                    break;

                case FOR:
                    feedback = defaultFeedback.forFB();
                    break;

                case FOREACH:
                    feedback = defaultFeedback.foreachFB();
                    break;

                case WHILE:
                    feedback = defaultFeedback.whileFB();
                    break;

                case CASE:
                    feedback = defaultFeedback.caseFB();
                    break;

                case METHOD:
                    feedback = defaultFeedback.methodFB();
                    break;

                case CONSTRUCTOR:
                    feedback = defaultFeedback.constructorFB();
                    break;
            }
        }
        replaceCoverage(statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, feedback);
    }

    @Override
    public String toString() {
        return "WantedFeedback{" +
                "type=" + type +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}

