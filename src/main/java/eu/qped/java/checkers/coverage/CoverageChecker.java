package eu.qped.java.checkers.coverage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.coverage.framework.coverage.Jacoco;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfCoverageSettings;
import eu.qped.java.checkers.mass.ShowFor;

public class CoverageChecker {

	private QfCoverageSettings covSettings;
	
	private Map<String, FeedbackMessage> feedbackMessages;
	private String fullCoverageReport;

	private File solutionRoot;

	public CoverageChecker(QfCoverageSettings covSettings, File solutionRoot) {
		this.covSettings = covSettings;
		this.solutionRoot = solutionRoot;
	}

	public void initializeFeedbackMessages() {
		if (feedbackMessages != null)
			return;
		
		AtomicInteger nextFree = new AtomicInteger(0);
		feedbackMessages = new HashMap<>();
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
		initializeFeedbackMessages();

        List<CoverageFacade> testClasses = new ArrayList<>();
        List<CoverageFacade> classes = new ArrayList<>();
        
        try {
			separateTestAndApplicationClasses(testClasses, classes);
		} catch (IOException e) {
			throw new RuntimeException("Could not create lists of test and application classes.", e);
		}

		TestFramework test = new JUnit5();
		Jacoco coverage = new Jacoco(test, solutionRoot);

		coverage.analyze(testClasses, classes);

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
		
		Set<FeedbackMessage> applicableFeedbackMessages = new LinkedHashSet<>();
		applicableFeedbackMessages.addAll(fullyMissedFeedbackMessages);
		applicableFeedbackMessages.addAll(partiallyMissedFeedbackMessages);
		
		Set<FeedbackMessage> suppressed = new LinkedHashSet<>();
		
		applicableFeedbackMessages.forEach(fm -> {
			fm.suppresses.forEach(id -> {
				suppressed.add(feedbackMessages.get(id));
			});
		});
		
		applicableFeedbackMessages.removeAll(suppressed);
		
		Stream<String> coverageResults = applicableFeedbackMessages.stream().map(fm -> "- " + fm.getMessage());
		Stream<String> fullCoverageReport = Stream.of(this.fullCoverageReport);
		Stream<String> testResults = coverage.getTestResults();
		
		List<Stream<String>> result = new ArrayList<>();

		if (covSettings.getShowTestFailures())
			result.add(testResults);

		if (!applicableFeedbackMessages.isEmpty()) {
			result.add(Stream.of("### Test Coverage Feedback"));
		}
		result.add(coverageResults);
		
		if (covSettings.getShowFullCoverageReport())
			result.add(fullCoverageReport);
		return result.stream().flatMap(s -> s).collect(Collectors.toList());
	}

	public void separateTestAndApplicationClasses(List<CoverageFacade> testClasses, List<CoverageFacade> classes)
			throws IOException {
		List<File> allClassFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "class");
        String solutionDirectoryPath = solutionRoot.getCanonicalPath() + File.separator;

        // The file separator will be used as regular expression by replaceAll.
        // Therefore, we must escape the separator on Windows systems.
        String fileSeparator = File.separator;
        if (fileSeparator.equals("\\")) {
        	fileSeparator = "\\\\";
        }
        for (File file : allClassFiles) {
        	String filename = file.getCanonicalPath();
        	
        	String classname = filename.
        			substring(solutionDirectoryPath.length(), filename.length() - ".class".length()).
        			replaceAll(fileSeparator, ".");

        	String[] classnameSegments = classname.split("\\.");
        	String simpleClassname = classnameSegments[classnameSegments.length - 1];
        	
        	CoverageFacade coverageFacade = new CoverageFacade(
        			Files.readAllBytes(file.toPath()),
        			classname);
        	
        	if (simpleClassname.startsWith("Test") 
        			|| simpleClassname.startsWith("test")
        			|| simpleClassname.endsWith("Test")
        			|| simpleClassname.endsWith("test")) {
        		// the class is a test
        		testClasses.add(coverageFacade);
        	} else {
        		// the class is an application class (i.e., no test)
        		classes.add(coverageFacade);
        	}
        }
	}

	public String getFullCoverageAndTestReport() {
		return fullCoverageReport;
	}
	
}
