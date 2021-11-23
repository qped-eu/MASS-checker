package eu.qped.umr;

import eu.qped.umr.qf.QfObject;

public class FailChecker implements Checker {

	public void check(QfObject qfObject) {

		throw new Error("This is what happens, when an uncaught exception occurs.");
		
	}
	
}
