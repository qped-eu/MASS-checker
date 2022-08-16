package eu.qped.java.checkers.coverage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.framework.coverage.Jacoco;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;

public class CoverageChecker {

	private final QfCovSetting setting;

	public CoverageChecker(QfCovSetting setting) {
		this.setting = setting;
	}

	public String[] check() {
		CoverageSetup.Data data = setting.getData();
		data.cleanUp();
		if (!data.isCompiled) {
			return data.syntaxFeedback.toArray(String[]::new);
		}

		TestFramework test = new JUnit5();
		Jacoco coverage = new Jacoco(test);

		coverage.analyze(data.testclasses, data.classes);

		List<Feedback> feedbacks = new ArrayList<>();
		coverage.getTestResults().forEach(tr -> feedbacks.add(new CoverageFeedback(tr.toString())));

		coverage.getModuleCoverageResults().forEach(mcr -> {
			mcr.getLinesNotCovered().forEach(
					line -> feedbacks.add(new CoverageFeedback(mcr.getModuleName() + ": not       covered: " + line)));
			mcr.getLinesPartiallyCovered().forEach(
					line -> feedbacks.add(new CoverageFeedback(mcr.getModuleName() + ": partially covered: " + line)));
		});

		return feedbacks.stream().map(Feedback::getBody).toArray(String[]::new);
	}

}
