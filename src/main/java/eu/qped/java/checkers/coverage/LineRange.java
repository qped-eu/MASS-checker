package eu.qped.java.checkers.coverage;

public class LineRange {

	public final int firstLine;
	public final int lastLine;

	public LineRange(int firstLine, int lastLine) {
		this.firstLine = firstLine;
		this.lastLine = lastLine;	
	}
	
	public boolean contains(int line) {
		return line >= firstLine && line <= lastLine;
	}
	
}
