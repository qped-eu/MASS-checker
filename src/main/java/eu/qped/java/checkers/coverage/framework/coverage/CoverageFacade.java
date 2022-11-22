package eu.qped.java.checkers.coverage.framework.coverage;

public class CoverageFacade {

    private final byte[] byteCode;
    private final String className;
    private final String content;

    public CoverageFacade(byte[] byteCode, String className, String content) {
        this.byteCode = byteCode;
        this.className = className;
        this.content = content;
    }

    public String className() {
        return className;
    }

    public byte[] byteCode() {
        return byteCode;
    }
    
    public String getContent() {
		return content;
	}
}
