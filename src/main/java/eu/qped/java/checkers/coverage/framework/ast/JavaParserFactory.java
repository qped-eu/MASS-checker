package eu.qped.java.checkers.coverage.framework.ast;

class JavaParserFactory implements AstFrameworkFactory {

    JavaParserFactory() {}

    @Override
    public AstFramework create() {
        return new JavaParser();
    }
}
