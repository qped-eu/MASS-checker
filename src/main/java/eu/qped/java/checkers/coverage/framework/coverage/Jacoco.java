package eu.qped.java.checkers.coverage.framework.coverage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import eu.qped.java.checkers.coverage.MemoryLoader;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;

public class Jacoco {

	protected final TestFramework testFramework;
	
	private List<String> testResults;
	
	private Set<ModuleCoverageResult> moduleCoverageResults;
	
	private boolean analysisPerformed = false;

	private String fullCoverageReport;

	public Jacoco(TestFramework testFramework) {
		this.testFramework = Objects.requireNonNull(testFramework,
				"ERROR::Jacoco.new() Parameter testFramework can't be null");
	}

	public void analyze(List<? extends CoverageFacade> testClasses,
			List<? extends CoverageFacade> classes) {
		
		if (analysisPerformed)
			throw new IllegalStateException("The code coverage analysis has already been performed. Now it is only allowed to access the results.");
		analysisPerformed = true;
		
		
		// Collect the coverage results
		CoverageBuilder coverageBuilder;
		try {
			// initialize JaCoCo Runtime
			IRuntime runtime = new LoggerRuntime();
			RuntimeData runtimeData = new RuntimeData();
			Instrumenter instrumenter = new Instrumenter(runtime);
			runtime.startup(runtimeData);
			
			// Load test and application classes using the JaCoCo in-memory classloader
			MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
			// test classes are not instrumented, as we don't want to measure coverage for them
			for (CoverageFacade testClass : testClasses) {
				memoryLoader.upload(testClass.className(), testClass.byteCode());
			}
			// instrument application classes so that we can measure coverage for them
			for (CoverageFacade clazz : classes) {
				memoryLoader.upload(clazz.className(), instrumenter.instrument(clazz.byteCode(), clazz.className()));
			}
			
			// run all tests using the JaCoCo class loaded
			testResults = testFramework.testing(
					testClasses.stream().map(CoverageFacade::className).collect(Collectors.toList()), memoryLoader);
			
			// finalize JaCoCo
			ExecutionDataStore executionDataStore = new ExecutionDataStore();
			runtimeData.collect(executionDataStore, new SessionInfoStore(), false);
			runtime.shutdown();
			
			coverageBuilder = new CoverageBuilder();
			Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);
			for (CoverageFacade clazz : classes) {
				analyzer.analyzeClass(clazz.byteCode(), clazz.className());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		moduleCoverageResults = new HashSet<>();
		
		StringBuilder fullCoverageReport = new StringBuilder("# Full Coverage Report\n\n");
		
		Map<String, String[]> codeByModulename = new HashMap<>();
		for (CoverageFacade clazz : classes) {
			codeByModulename.put(clazz.className().replace('.','/') + ".java", clazz.getContent().split("\\n"));
		}

		
		// Consider all source files and determine which lines were covered, partially covered or not covered
		for (ISourceFileCoverage sfCoverage : coverageBuilder.getSourceFiles()) {
			String packagePath = "";
			if (!sfCoverage.getPackageName().isEmpty())
				packagePath = sfCoverage.getPackageName().replace('.', '/') + "/";

			String fullModuleName = packagePath + sfCoverage.getName();
					
			fullCoverageReport.append("## ").append(fullModuleName).append("\n\n");
			fullCoverageReport.append("| Line | Coverage type | Code |\n");
			fullCoverageReport.append("| ---: | :------------ | ---- |\n");
						
			ModuleCoverageResult result = new ModuleCoverageResult(packagePath + sfCoverage.getName());
			moduleCoverageResults.add(result);
			String[] code = codeByModulename.get(fullModuleName);
			for (int i = 1; i <= code.length; i++) {
				fullCoverageReport.append("| ").append(i).append(" | ");

				ILine line = sfCoverage.getLine(i);
				String backgroundColor;
				switch (line.getStatus()) {
				case ICounter.FULLY_COVERED:
					fullCoverageReport.append("_FULLY covered_");
					backgroundColor = "#A9DFBF"; // green
					result.linesFullyCovered.add(i);
					break;
				case ICounter.PARTLY_COVERED:
					fullCoverageReport.append("_**PARTIALLY covered**_");
					backgroundColor = "#F9E79F"; // yellow
					result.linesPartiallyCovered.add(i);
					break;
				case ICounter.NOT_COVERED:
					fullCoverageReport.append("**NOT covered**");
					backgroundColor = "#F5B7B1"; // red
					result.linesNotCovered.add(i);
					break;
				case ICounter.EMPTY:
					fullCoverageReport.append("_EMPTY_");
					backgroundColor = "#A9DFBF"; // green
					result.linesEmpty.add(i);
					break;

				default:
					throw new RuntimeException("Unknown line status '" + line.getStatus() + "'. Perhaps it was introduced by a newer version of JaCoCo.");
				}
				if (code != null && i <= code.length) {
					fullCoverageReport.append(" | <span style='background-color:").append(backgroundColor).append("'>");

					if (!code[i - 1].isEmpty())
						fullCoverageReport.append("`").append(code[i - 1].replace(' ', '\u00A0').replace("\t", "\u00A0\u00A0\u00A0\u00A0")).append("`");
					
					fullCoverageReport.append("</span> |\n");
				} else {
					fullCoverageReport.append(" | |\n");
				}
			}
		}
		
		this.fullCoverageReport = fullCoverageReport.toString();
	}

	public Stream<ModuleCoverageResult> getModuleCoverageResults() {
		return moduleCoverageResults.stream();
	}
	
	public Stream<String> getTestResults() {
		return testResults.stream();
	}
	
	public String getFullCoverageReport() {
		return fullCoverageReport;
	}
	
}
