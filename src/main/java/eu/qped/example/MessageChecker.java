package eu.qped.example;

import eu.qped.framework.Checker;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;

public class MessageChecker implements Checker {

	@QfProperty
	private String message;
	
	@Override
	public void check(QfObject qfObject) throws Exception {
		System.out.println("Hello");
		System.out.println(message);
		
		qfObject.getAnswer();
		
		String[] feedback = new String[1];
		feedback[0] = message;
		qfObject.setFeedback(feedback);
	}

}
