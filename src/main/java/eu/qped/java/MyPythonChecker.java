package eu.qped.java;

import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;

public class MyPythonChecker implements Checker {

	@Override
	public void check(QfObject qfObject) throws Exception {
		qfObject.setFeedback(new String[] {"Hello Python"});
		
		
	}

}
