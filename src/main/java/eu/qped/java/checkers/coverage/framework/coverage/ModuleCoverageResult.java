package eu.qped.java.checkers.coverage.framework.coverage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ModuleCoverageResult {
	
	private final String moduleName;
	final Set<Integer> linesEmpty = new HashSet<>();
	final Set<Integer> linesNotCovered = new HashSet<>();
	final Set<Integer> linesPartiallyCovered = new HashSet<>();
	final Set<Integer> linesFullyCovered = new HashSet<>();
	
	public ModuleCoverageResult(String moduleName) {
		super();
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public Stream<Integer> getLinesEmpty() {
		return linesEmpty.stream();
	}

	public Stream<Integer> getLinesNotCovered() {
		return linesNotCovered.stream();
	}

	public Stream<Integer> getLinesPartiallyCovered() {
		return linesPartiallyCovered.stream();
	}

	public Stream<Integer> getLinesFullyCovered() {
		return linesFullyCovered.stream();
	}

	
}
