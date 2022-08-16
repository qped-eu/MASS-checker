package eu.qped.java.checkers.coverage.framework.coverage;

import java.util.HashSet;
import java.util.List;
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
		
		// Consider all source files and determine which lines were covered, partially covered or not covered
		for (ISourceFileCoverage sfCoverage : coverageBuilder.getSourceFiles()) {
			ModuleCoverageResult result = new ModuleCoverageResult(sfCoverage.getName());
			moduleCoverageResults.add(result);
			for (int i = sfCoverage.getFirstLine(); i <= sfCoverage.getLastLine(); i++) {
				ILine line = sfCoverage.getLine(i);
				switch (line.getStatus()) {
				case ICounter.FULLY_COVERED:
					result.linesFullyCovered.add(i);
					break;
				case ICounter.PARTLY_COVERED:
					result.linesPartiallyCovered.add(i);
					break;
				case ICounter.NOT_COVERED:
					result.linesNotCovered.add(i);
					break;
				case ICounter.EMPTY:
					result.linesEmpty.add(i);
					break;

				default:
					throw new RuntimeException("Unknown line status '" + line.getStatus() + "'. Perhaps it was introduced by a newer version of JaCoCo.");
				}
			}
		}
	}

	public Stream<ModuleCoverageResult> getModuleCoverageResults() {
		return moduleCoverageResults.stream();
	}
	
	public Stream<String> getTestResults() {
		return testResults.stream();
	}
	
}
