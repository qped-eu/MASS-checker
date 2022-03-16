package eu.qped.example;

import eu.qped.framework.checker.Checker;
import eu.qped.framework.qf.QfObject;

public class SimpleChecker implements Checker {

	public void check(QfObject qfObject) {
		
		qfObject.setCondition("myCondition", qfObject.getAnswer().indexOf("simple") >= 0);
		
		qfObject.setMessage("myMessage", "The answer is " + qfObject.getAnswer().length() + " characters long.");

	}
	
}
