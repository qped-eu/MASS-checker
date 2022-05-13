package eu.qped.java.checkers.coverage.framework.coverage;


/**
 * {@link CoverageFrameworkFactoryAbstract} is a abstract factory that creates a {@link CoverageFrameworkFactory}.
 * Supported frameworks:
 * <ul>
 *     <li>JACOCO := Creates a factory that implements a JaCoco api.</li>
 * </ul>
 *
 * @author  Herfurth
 */
public class CoverageFrameworkFactoryAbstract {
    protected static String[] framework = new String[] {"JACOCO"};

    public static CoverageFrameworkFactory create(String framework) {
        switch(framework) {
            case "JACOCO" :
                return new JacocoFactory();

            default:
                throw new IllegalStateException(String.format("ERROR::CoverageFrameworkFactoryAbstract.create(%s) is not a valid framework", framework));
        }
    }

}
