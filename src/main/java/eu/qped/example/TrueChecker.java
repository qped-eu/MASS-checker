package eu.qped.example;

import eu.qped.framework.checker.Checker;
import eu.qped.framework.qf.QfObject;

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
