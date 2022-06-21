package eu.qped.java.checkers.coverage.framework.test;


/**
 * {@link TestFrameworkFactoryAbstract} is a abstract factory that creates a {@link TestFrameworkFactory}.
 * Supported frameworks:
 * <ul>
 *     <li>JUNIT5 := Creates a factory that implements a Junit 5 test.</li>
 * </ul>
 * @author  Herfurth
 */
public class TestFrameworkFactoryAbstract {
    protected static String[] frameworks = new String[] {"JUNIT5"};

    public static TestFrameworkFactory create(String framework) {
        switch (framework) {
            case "JUNIT5" :
                return new JUnit5Factory();

            default:
                throw new IllegalStateException(String.format("ERROR::TestFrameworkFactory.create(%s) is not a valid framework", framework));
        }
    }

}
