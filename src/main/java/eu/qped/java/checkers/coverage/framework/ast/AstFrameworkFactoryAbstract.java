package eu.qped.java.checkers.coverage.framework.ast;


public class AstFrameworkFactoryAbstract {

    protected static String[] frameworks = new String[] {"JAVA_PARSER"};

    public static AstFrameworkFactory create(String framework) {
        switch(framework) {
            case "JAVA_PARSER" :
                return new JavaParserFactory();

            default:
                throw new IllegalStateException(String.format("ERROR::AstFrameworkFactoryAbstract.create(%s) is not a valid framework", framework));
        }
    }

}
