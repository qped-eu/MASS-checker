package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.feedback.wanted.ProviderWF;
import eu.qped.java.checkers.coverage.feedback.wanted.WantedFeedback;
import eu.qped.java.checkers.coverage.framework.test.TestResult;

import java.util.Objects;

/**
 * All information stored related to a test is stored in this class.
 */
public class TestFB extends Feedback {
    private final TestResult result;

    public TestFB(TestResult result) {
        super("");
        this.result = Objects.requireNonNull(result);
    }

    public String className() {
        return result.className();
    }

    public String methodName() {
        return result.methodName();
    }

    public String want() {
        return result.want();
    }

    public String got() {
        return result.got();
    }

    void createFeedback(ProviderWF provider) {
        WantedFeedback wanted = provider.provide(true, className(), methodName());
        if (Objects.nonNull(wanted)) {
            wanted.setFeedbackBody(this);
        }
    }

    @Override
    public String toString() {
        return "TestFB{" +
                "result=" + result +
                '}';
    }

}

