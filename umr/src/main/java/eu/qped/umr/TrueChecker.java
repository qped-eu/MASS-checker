package eu.qped.umr;

import eu.qped.umr.qf.QfObject;

public class TrueChecker implements Checker {

	private String condition;

	public TrueChecker(String condition) {
		this.condition = condition;
	}

	@Override
	public void check(QfObject qfObject) throws Exception {
		qfObject.setCondition(condition, true);
	}

}
