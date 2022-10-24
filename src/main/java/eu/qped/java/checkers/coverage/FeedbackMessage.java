package eu.qped.java.checkers.coverage;

import java.util.List;

import eu.qped.java.checkers.mass.ShowFor;

public class FeedbackMessage {

	public final String id;
	public final String moduleName;
	public final List<LineRange> lineRanges;
	public final ShowFor showFor;
	public final String message;
	public final List<String> suppresses;

	public FeedbackMessage(String id, String moduleName, List<LineRange> lineRanges, ShowFor showFor, String message, List<String> suppresses) {
		this.id = id;
		this.moduleName = moduleName;
		this.lineRanges = lineRanges;
		this.showFor = showFor;
		this.message = message;
		this.suppresses = suppresses;
	}
	
	public String getMessage() {
		return message;
	}

	public boolean isContained(String moduleName, int line) {
		if (!this.moduleName.equals(moduleName))
			return false;
		
		for (LineRange range : lineRanges) {
			if (range.contains(line))
				return true;
		}
		
		return false;
	}
}
