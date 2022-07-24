package eu.qped.java.checkers.metrics.res;

/**
 * @author Jannik Seus
 */
class TestMetrics2 {

    static int i = 0;
    static int j = 0;

    protected TestMetrics mKt = null;

    void m1() {
        mKt = new TestMetrics();
        i = 1;
        try {
            i += 17;
            throw new Exception(Integer.toHexString(i));
        } catch (Exception ex) {
            if (ex != null) System.err.println(ex.toString());
        }
    }

    void m2() {
        m1();
    }

    int m3(int jk) {
        m1();
        jk = j;
        return jk;
    }

}
