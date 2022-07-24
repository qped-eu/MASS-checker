package eu.qped.java.checkers.metrics.res;

/**
 * @author Jannik Seus
 */
abstract class DummyClass {

    abstract protected void dummyMethod();

    public void realMethod() {
        System.out.println("I'm doing something very important!");
    }
}
