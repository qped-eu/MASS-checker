package eu.qped.java.checkers.coverage.framework.test;

class JUnit5Factory implements TestFrameworkFactory {

    @Override
    public TestFramework create() {
        return new JUnit5();
    }

}
