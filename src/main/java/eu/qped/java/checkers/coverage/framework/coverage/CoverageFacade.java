package eu.qped.java.checkers.coverage.framework.coverage;

public class CoverageFacade {

    private final byte[] byteCode;
    private final String className;

    public CoverageFacade(byte[] byteCode, String className) {
        this.byteCode = byteCode;
        this.className = className;
    }

    public String className() {
        return className;
    }

    public byte[] byteCode() {
        return byteCode;
    }

}
