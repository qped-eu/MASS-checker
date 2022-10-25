package eu.qped.java.checkers.metrics.res;

/**
 * @author Jannik Seus
 */
class TestMetricsParent {

    protected int image;

    public TestMetricsParent(int id) {
        image = id;
    }

    public int sru() {
        new TestMetrics().m1();
        return image++;
    }

}
