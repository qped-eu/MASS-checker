package eu.qped.umr;

import eu.qped.umr.qf.QfObject;

public class SimpleChecker implements Checker {

	public void check(QfObject qfObject) {
		
		qfObject.setCondition("myCondition", qfObject.getAnswer().indexOf("simple") >= 0);
		
		qfObject.setMessage("myMessage", "The answer is " + qfObject.getAnswer().length() + " characters long.");

	}
	
}
