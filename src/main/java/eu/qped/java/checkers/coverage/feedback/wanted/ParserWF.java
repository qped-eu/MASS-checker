package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import eu.qped.java.checkers.coverage.feedback.DefaultFB;

import java.util.*;



/**
 * Parses all {@link WantedFeedback} from a list of strings.
 * @author Herfurth
 * @version 1.0
 */
public class ParserWF {
    private final ParserDefaultWF defaultWF = new ParserDefaultWF();
    private final ParserCustomWF customWF = new ParserCustomWF();
    private Map<String, ProviderByClassWF> classByClassname;
    private DefaultFB defaultFeedback;
    private boolean wantAllTest, wantAllCoverage;

    /**
     * Parses all encoded feedback and stores it in {@link ProviderWF}.
     * Definition encoding:
     * <ul>
     *     <li> [EMPTY] : [{@link FeedbackType#TEST} | {@link FeedbackType#COVERAGE}] </li>
     *     <li> [CLASSNAME] : [{@link FeedbackType#TEST} | {@link FeedbackType#COVERAGE}] </li>
     *     <li> [CLASSNAME] : [{@link FeedbackType}] : [METHODNAME | LINE INDEX]: [MESSAGE | EMPTY] </li>
     * </ul>
     * @param feedback all feedback decoded als string
     */
    public ProviderWF parse(String language, List<String> feedback) {
        classByClassname = new HashMap<>();
        defaultFeedback = DefaultFB.load(language);
        wantAllCoverage = false;
        wantAllTest = false;

        for (String f : feedback) {
            if (customWF.parse(f)) {
                parseCustomWF();
            } else if (defaultWF.parse(f)) {
                parseDefaultWF(pick(defaultWF.type()));
            }
        }
        return new ProviderWF(classByClassname, wantAllTest, wantAllCoverage, defaultFeedback);
    }

    private void parseCustomWF() {
        String className = customWF.className();
        String identifier = customWF.identifier();
        if (className.isBlank() || identifier.isBlank()) {
            throw new IllegalStateException();
        }
        ProviderByClassWF classWF = provide(classByClassname, customWF.className());
        classWF.feedbackByIdentifier.put(identifier,
                new WantedFeedback(
                        defaultFeedback,
                        customWF.type(),
                        customWF.customWF()));
    }

    private static ProviderByClassWF provide(Map<String, ProviderByClassWF> classByClassname, String className) {
        ProviderByClassWF classWF = classByClassname.get(className);
        if (Objects.isNull(classWF)) {
            classWF = new ProviderByClassWF();
            classByClassname.put(className, classWF);
        }
        return classWF;
    }

    @FunctionalInterface
    private interface ForAll {
        void set();
    }

    private void parseDefaultWF(ForAll f) {
        if (defaultWF.className().isBlank()) {
            f.set();
        } else {
            provide(classByClassname, defaultWF.className()).defaultType = defaultWF.type();
        }
    }

    private  ForAll pick(FeedbackType type) {
        if (type.equals(FeedbackType.TEST)) {
            return () -> { wantAllTest = true; };
        } else {
            return () -> { wantAllCoverage = true; };
        }
    }

}
