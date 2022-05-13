package eu.qped.java.checkers.coverage.feedback;

import eu.qped.java.checkers.coverage.framework.ast.AstMethod;
import eu.qped.java.checkers.coverage.framework.coverage.*;


public class NodeBuilder {

    protected class AstMethodNode extends Node<AstMethod> {
        final String key;
        public AstMethodNode(AstMethod value) {
            super(value);
            if (value.isConstructor()) {
                key = value.start() + "";
            } else {
                key = value.methodName();
            }
        }

        @Override
        String keyByClass() {
            return value.className();
        }

        @Override
        String className() {
            return value.className();
        }

        @Override
        String keyByMethod() {
            return key;
        }

        @Override
        String methodName() {
            return value.methodName();
        }

        @Override
        void insert(ByMethod byMethod) {
            byMethod.content = value;
        }

        @Override
        void insert(ByClass byClass) {
            if (value.isConstructor())
                byClass.constructorKeys.add(keyByMethod());

            super.insert(byClass);
        }
    }

    protected class StmtFBNode extends Node<StmtFB> {
        public StmtFBNode(StmtFB value) {
            super(value);
        }

        @Override
        String keyByClass() {
            return value.className();
        }

        @Override
        String className() {
            return value.className();
        }

        @Override
        String keyByMethod() {
            return value.methodName();
        }

        @Override
        String methodName() {
            return value.methodName();
        }

        @Override
        void insert(ByMethod byMethod) {
            if (! byMethod.statementsFB.isEmpty() && byMethod.statementsFB.getLast().isInner(value)) {
                byMethod.statementsFB.getLast().insert(value);
            } else {
                byMethod.statementsFB.addLast(value);
            }
        }
    }

    protected class CoverageClassNode extends Node<CoverageClass> {

        public CoverageClassNode(CoverageClass value) {
            super(value);
        }

        @Override
        String keyByClass() {
            return value.className();
        }

        @Override
        String className() {
            return value.className();
        }

        @Override
        String keyByMethod() {
           throw new IllegalStateException();
        }

        @Override
        String methodName() {
            throw new IllegalStateException();
        }

        @Override
        void insert(ByClass byClass) {
            byClass.aClass = value;
        }

        @Override
        void insert(ByMethod byMethod) {
            throw new IllegalStateException();
        }
    }

    protected class CoverageMethodNode extends Node<CoverageMethod> {
        public CoverageMethodNode(CoverageMethod value) {
            super(value);
        }

        @Override
        String keyByClass() {
            return value.className();
        }

        @Override
        String className() {
            return value.className();
        }

        @Override
        String keyByMethod() {
            return value.methodName();
        }

        @Override
        String methodName() {
            return value.methodName();
        }

        @Override
        void insert(ByMethod byMethod) {
            byMethod.coverage = value;
        }
    }



    Node build(AstMethod value) {
        return new AstMethodNode(value);
    }

    Node build(StmtFB value) {
        return new StmtFBNode(value);
    }

    Node build(CoverageMethod value) {
        return new CoverageMethodNode(value);
    }

    Node build(CoverageClass value) {
        return new CoverageClassNode(value);
    }

}
