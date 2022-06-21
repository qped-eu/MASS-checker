package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.enums.StatementType;
import eu.qped.java.checkers.coverage.feedback.wanted.*;
import eu.qped.java.checkers.coverage.framework.ast.AstResult;
import java.util.*;


/**
 * All information stored related to a statement is stored in this class.
 */
public class StmtFB extends Feedback {
    final LinkedList<StmtFB> statementsFB = new LinkedList<>();
    final AstResult result;

    StmtFB(AstResult result) {
        super("");
        this.result = Objects.requireNonNull(result);
    }

    public int start() {
        return result.start();
    }

    public int end() {
        return result.end();
    }

    public String className() {
        return result.className();
    }

    public String methodName() {
        return result.methodName();
    }

    public StatementType type() {
        return result.type();
    }

    void createFeedback(ProviderWF provider) {
        WantedFeedback wanted = provider.provide(false, className(), start()+"");
        if (Objects.nonNull(wanted)) {
            wanted.setFeedbackBody(this);
        }
    }

    void insert(StmtFB stmt) {
        if (! statementsFB.isEmpty() && statementsFB.getLast().isInner(stmt)) {
            statementsFB.getLast().insert(stmt);
        } else {
            statementsFB.add(stmt);
        }
    }

    boolean isInner(StmtFB other) {
        return result.start() <= other.result.start() && other.result.end() <= result.end();
    }

    @Override
    public String toString() {
        return "StmtFB{" +
                "statementsFB=" + statementsFB +
                ", result=" + result +
                '}';
    }

}
