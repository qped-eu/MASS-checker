package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import eu.qped.java.checkers.coverage.feedback.DefaultFB;
import java.util.*;


/**
 * Stores {@link WantedFeedback} into a tree structure.
 * @author Herfurth
 * @version 1.0
 */
public class ProviderWF {
    /**
     * Default feedback for the FeedbackTypes:
     * <ul>
     *     <li>{@link FeedbackType#COVERAGE}</li>
     *     <li>{@link FeedbackType#TEST}</li>
     * </ul>
     */
    public static final WantedFeedback TEST_WF = new WantedFeedback(FeedbackType.TEST, ""), COVERAGE_WF = new WantedFeedback(FeedbackType.COVERAGE, "");

    private final Map<String, ProviderByClassWF> classByClassname;
    private final boolean wantAllTest, wantAllCoverage;

    ProviderWF(Map<String, ProviderByClassWF> classByClassname, boolean wantAllTest, boolean wantAllCoverage, DefaultFB defaultFeedback) {
        this.classByClassname = classByClassname;
        this.wantAllTest = wantAllTest;
        this.wantAllCoverage = wantAllCoverage;
        TEST_WF.setDefaultFeedback(defaultFeedback);
        COVERAGE_WF.setDefaultFeedback(defaultFeedback);
    }

    /**
     * GetWanted returns for a result and identifier (method name or line index) the {@link WantedFeedback}
     */
    public WantedFeedback provide(boolean isTest, String className, String identifier) {
        ProviderByClassWF feedbackClass = classByClassname.get(className);
        WantedFeedback wanted = null;

        if (Objects.nonNull(feedbackClass))  wanted = feedbackClass.provide(identifier);

        if (Objects.isNull(wanted)) {
            if (wantAllTest && isTest) {
                wanted = TEST_WF;
            } else if (wantAllCoverage && ! isTest) {
                wanted = COVERAGE_WF;
            }
        }
        return wanted;
    }

    protected Map<String, ProviderByClassWF> classByClassname() {
        return classByClassname;
    }

    protected boolean wantsAllTest() {
        return wantAllTest;
    }

    protected boolean wantsAllCoverage() {
        return wantAllCoverage;
    }
}
