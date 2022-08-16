package eu.qped.java.checkers.coverage;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckerRunner;

class CoverageBagTest {

	@Test
	void test() throws IOException {
		CheckerRunner.main(new String[]{"src/test/resources/system-tests/coverage/tutorial/qf-input.json"});
		
	}

}
