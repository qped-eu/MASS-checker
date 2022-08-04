package eu.qped.java.checkers.coverage.feedback.wantMap;

import eu.qped.java.checkers.coverage.feedback.DefaultFB;
import eu.qped.java.checkers.coverage.feedback.SummaryMapped;
import eu.qped.java.checkers.coverage.feedback.TestFB;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Provider {
    protected static String REPLACE_CLASS = "CLASS", REPLACE_METHOD = "METHOD";
    protected static String REPLACE_WANT = "WANT", REPLACE_GOT = "GOT";

    private final Map<String, String> wantedFeedback;
    private final Set<String> testFeedback;
    private final DefaultFB defaultFB;
    private final boolean testFeedbackForAll;

    public Provider(Map<String, String> wantedFeedback, Set<String> testFeedback, DefaultFB defaultFB, boolean testFeedbackForAll) {
        this.wantedFeedback = Objects.requireNonNull(wantedFeedback);
        this.testFeedback = Objects.requireNonNull(testFeedback);
        this.defaultFB = Objects.requireNonNull(defaultFB);
        this.testFeedbackForAll = Objects.requireNonNull(testFeedbackForAll);
    }

    public boolean setFeedback(TestFB testFB) {
        if (wantedFeedback.containsKey(testFB.className()+testFB.methodName())) {
            String fb = wantedFeedback.get(testFB.className()+testFB.methodName());
            testFB.setBody(replaceHead(testFB.className(), testFB.methodName(), fb));
            if (testFB.hasFailedWithoutAssertion())
                return true;

            testFB.setBody(replaceTail(testFB.want(), testFB.got(), testFB.getBody()));
            return true;
        } else if (testFeedbackForAll || testFeedback.contains(testFB.className())) {
            if (testFB.hasFailedWithoutAssertion()) {
                testFB.setBody(replaceHead(
                        testFB.className(),
                        testFB.methodName(),
                        defaultFB.testFailedFB()));
            } else {
                testFB.setBody(replaceTail(
                        testFB.want(),
                        testFB.got(),
                        replaceHead(
                                testFB.className(),
                                testFB.methodName(),
                                defaultFB.testFB())));
            }
            return true;
        }
        return false;
    }

    private String replaceHead(String classname, String methodname, String wf) {
        return wf.replace(REPLACE_CLASS, classname)
                .replace(REPLACE_METHOD, methodname);
    }

    private String replaceTail(String want, String got, String wf) {
        return wf.replace(REPLACE_WANT, want)
                .replace(REPLACE_GOT, got);
    }

    public boolean setFeedback(SummaryMapped.LineFB feedback) {
        String fb = wantedFeedback.get(feedback.className+feedback.index);
        if (Objects.nonNull(fb)) {
            feedback.setBody(fb);
            return true;
        }
        return false;
    }

}
