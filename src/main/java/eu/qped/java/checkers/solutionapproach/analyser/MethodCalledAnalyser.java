package eu.qped.java.checkers.solutionapproach.analyser;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

public class MethodCalledAnalyser {

    private final BlockStmt methodBody;
    private final ArrayList<String> calledMethods;

    private MethodCalledAnalyser(BlockStmt methodBody) {
        this.methodBody = methodBody;
        this.calledMethods = new ArrayList<>();
        walk();
    }

    public static MethodCalledAnalyser createRecursiveCheckHelper(BlockStmt methodBody) {
        return new MethodCalledAnalyser(methodBody);
    }

    public boolean check(String methodName) {
        return calledMethods.contains(methodName);
    }

    private void walk() {
        if (methodBody.getChildNodes() != null) {
            methodBody.accept(new VoidVisitorAdapter<Void>() {

                @Override
                public void visit(MethodCallExpr n, Void arg) {
                    super.visit(n, arg);
                    calledMethods.add(n.getName().toString());
                }
            }, null);
        }
    }
}
