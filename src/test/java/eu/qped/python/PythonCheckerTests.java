package eu.qped.python;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({ "eu.qped.python.syntax", "eu.qped.python.style", "eu.qped.python.semantics", "eu.qped.python.testing",
		"eu.qped.python.metrics" })
public class PythonCheckerTests {

}
