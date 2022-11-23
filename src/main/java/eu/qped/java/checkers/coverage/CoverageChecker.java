package eu.qped.java.checkers.coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.qped.java.checkers.coverage.framework.coverage.Jacoco;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfCoverageSettings;
import eu.qped.java.checkers.mass.ShowFor;

public class CoverageChecker {

	private QfCoverageSettings covSetting;
	private CoverageSetup coverageSetup;
	
	private final Map<String, FeedbackMessage> feedbackMessages = new HashMap<>();
	private String fullCoverageReport;

	public CoverageChecker(QfCoverageSettings covSettings, CoverageSetup coverageSetup) {
		this.covSetting = covSettings;
		this.coverageSetup = coverageSetup;
		
		AtomicInteger nextFree = new AtomicInteger(0);
		
		covSettings.getFeedback().forEach(
				messageSetting -> {
					String id = messageSetting.getId();
					if (id == null || id.isBlank()) {
						id = "$unnamed$_" + nextFree.getAndIncrement();
					}
					
					String fileName = messageSetting.getFileName();
					
					ShowFor showFor = messageSetting.getShowFor();
					
					String message = messageSetting.getMessage();
					
					String lineRanges = messageSetting.getLineRanges();
					if (lineRanges == null)
						throw new IllegalArgumentException("Line Range must not be empty");
					
					String[] lineRangesArray = lineRanges.split(",");
					
					List<LineRange> lineRangeList = new ArrayList<>();
					for (String range : lineRangesArray) {
						int lower, upper;
						String[] boundaries = range.split("-");
						if (boundaries.length == 0)
							throw new IllegalArgumentException("A line range must contain at least one number.");
						lower = Integer.parseInt(boundaries[0].trim());
						if (boundaries.length == 2) {
							upper = Integer.parseInt(boundaries[1].trim());
						}
						else {
							upper = lower;
						}
						if (boundaries.length > 2)
							throw new IllegalArgumentException("A line range must contain at most two numbers.");
						lineRangeList.add(new LineRange(lower, upper));
					}
					
					String suppresses = messageSetting.getSuppresses();
					List<String> suppressesList = new ArrayList<>();
					if (suppresses != null) {
						for (String suppressesId : suppresses.split(",")) {
							if (!suppressesId.isBlank()) {
								suppressesList.add(suppressesId.trim());
							}
						}
					}
					
					FeedbackMessage feedbackMessage = new FeedbackMessage(id, fileName, lineRangeList, showFor, message, suppressesList);
					feedbackMessages.put(id, feedbackMessage);
				}
		);
	}

	public List<String> check() {
		CoverageSetup data = coverageSetup;
		if (!data.isCompiled) {
			return data.syntaxFeedback;
		}

		TestFramework test = new JUnit5();
		Jacoco coverage = new Jacoco(test);

		coverage.analyze(data.testclasses, data.classes);

		StringBuilder fullReport = new StringBuilder();
		fullReport.append(coverage.getFullCoverageReport());
		this.fullCoverageReport = fullReport.toString();
		
		List<FeedbackMessage> fullyMissedFeedbackMessages = new ArrayList<>();
		List<FeedbackMessage> partiallyMissedFeedbackMessages = new ArrayList<>();
		feedbackMessages.values().forEach(fm -> {
			if (fm.showFor == ShowFor.FULLY_MISSED)
				fullyMissedFeedbackMessages.add(fm);
			else if (fm.showFor == ShowFor.PARTIALLY_MISSED)
				partiallyMissedFeedbackMessages.add(fm);
		});
		
		
//		if (!this.moduleName.equals(moduleName))
//			return false;
		
		coverage.getModuleCoverageResults().forEach(mcr -> {
			// fully missed feedback messages:
			// if a line that is at least partly covered is in the range of a feedback message that should only be shown
			// for fully missed ranges, that feedback message is not applicable.
			Stream.concat(mcr.getLinesFullyCovered(), mcr.getLinesPartiallyCovered()).forEach(line -> {
				ListIterator<FeedbackMessage> it = fullyMissedFeedbackMessages.listIterator();
				while (it.hasNext()) {
					FeedbackMessage fm = it.next();
					if (fm.isContained(mcr.getModuleName(), line))
						it.remove();
				}
			});

			// partially missed feedback messages:
			// For each feedback message search if there is at least one line in its range that is not covered or
			// partially covered.
			// if no such line is found, the feedback message is not applicable
			ListIterator<FeedbackMessage> it = partiallyMissedFeedbackMessages.listIterator();
			while (it.hasNext()) {
				FeedbackMessage fm = it.next();
				
				if (!fm.moduleName.equals(mcr.getModuleName()))
					continue;

				// if at least one not covered or partially covered line is found, this is sufficient
				// skip to next feedback message and don't remove this one
				if (!Stream.concat(mcr.getLinesNotCovered(), mcr.getLinesPartiallyCovered()).
						anyMatch(line -> fm.isContained(mcr.getModuleName(), line))) {
					
					// no partially/fully missed lines found -> remove feedback message
					it.remove();
				}
			}


		});
		
		Set<FeedbackMessage> applicableFeedbackMessages = new HashSet<>();
		applicableFeedbackMessages.addAll(fullyMissedFeedbackMessages);
		applicableFeedbackMessages.addAll(partiallyMissedFeedbackMessages);
		
//		coverage.getModuleCoverageResults().forEach(mcr -> {
//			mcr.getLinesNotCovered().forEach(line -> {
//				feedbackMessages.values().forEach(fm -> {
//					if (fm.matches(mcr.getModuleName(), line, CoverageType.NON))
//							applicableFeedbackMessages.add(fm);
//				});
//			});
//			mcr.getLinesPartiallyCovered().forEach(line -> {
//				feedbackMessages.values().forEach(fm -> {
//					if (fm.matches(mcr.getModuleName(), line, CoverageType.PARTIAL))
//							applicableFeedbackMessages.add(fm);
//				});
//			});
//		});

		Set<FeedbackMessage> suppressed = new HashSet<>();
		
		applicableFeedbackMessages.forEach(fm -> {
			fm.suppresses.forEach(id -> {
				suppressed.add(feedbackMessages.get(id));
			});
		});
		
		applicableFeedbackMessages.removeAll(suppressed);
		
		Stream<String> coverageResults = applicableFeedbackMessages.stream().map(FeedbackMessage::getMessage);
		Stream<String> fullCoverageReport = Stream.of(this.fullCoverageReport);
		Stream<String> testResults = coverage.getTestResults();
		
		List<Stream<String>> result = new ArrayList<>();

		if (covSetting.getShowTestFailures())
			result.add(testResults);
		result.add(coverageResults);
		if (covSetting.getShowFullCoverageReport())
			result.add(fullCoverageReport);
		return result.stream().flatMap(s -> s).collect(Collectors.toList());
	}

	public String getFullCoverageAndTestReport() {
		return fullCoverageReport;
	}
	
}
