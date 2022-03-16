package eu.qped.example;

import eu.qped.framework.checker.Checker;
import eu.qped.framework.qf.QfObject;

public class FailChecker implements Checker {

	public void check(QfObject qfObject) {

		throw new Error("This is what happens, when an uncaught exception occurs.");
		
	}
	
}
