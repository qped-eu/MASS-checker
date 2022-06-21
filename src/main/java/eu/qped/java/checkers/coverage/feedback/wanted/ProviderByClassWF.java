package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Is parte of the {@link ProviderWF} tree structure.
 * Is only real purpose is to save the {@link #defaultType}.
 * @author Herfurth
 * @version 1.0
 */
class ProviderByClassWF {
    final Map<String, WantedFeedback> feedbackByIdentifier = new HashMap<>();
    FeedbackType defaultType;

    ProviderByClassWF() {}

    /**
     * GetWanted returns for identifier (method name or line index) the {@link WantedFeedback}
     */
    WantedFeedback provide(String identifier) {
        WantedFeedback wanted = feedbackByIdentifier.get(identifier);
        if (Objects.isNull(wanted) && Objects.nonNull(defaultType)) {
            if (defaultType.equals(FeedbackType.COVERAGE)) {
                wanted = ProviderWF.COVERAGE_WF;
            } else if (defaultType.equals(FeedbackType.TEST)) {
                wanted = ProviderWF.TEST_WF;
            }
        }
        return wanted;
    }
}
