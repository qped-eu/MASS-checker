package eu.qped.java.checkers.coverage.framework.test;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

/**
 * JUnit 5 is used to run a set of test classes.
 * @author Herfurth
 * @version 1.0
 * @see <a href="https://junit.org/junit5/docs/current/user-guide/#launcher-api">JUnit5 API</a>
 */
public class JUnit5 implements TestFramework {

    public JUnit5() {
    }

    @Override
    public List<String> testing(List<String> testClasses, ClassLoader loader) {
        Objects.requireNonNull(testClasses, "testClasses must not be null");
        Objects.requireNonNull(loader, "loader must not be null");
		
        Thread.currentThread().setContextClassLoader(loader);
		
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
		        .request()
		        .selectors(testClasses.stream().map(t -> selectClass(t)).collect(Collectors.toList()))
		        .build();
		
		SummaryGeneratingListener sgl = new SummaryGeneratingListener();
		LauncherConfig launcherConfig = LauncherConfig
		        .builder()
		        .enableTestEngineAutoRegistration(false)
		        .enableTestExecutionListenerAutoRegistration(false)
		        .addTestExecutionListeners(sgl)
		        .addTestEngines(new JupiterTestEngine())
		        .build();
		
		Launcher launcher = LauncherFactory.create(launcherConfig);		
		launcher.execute(launcher.discover(request));
		
        TestExecutionSummary summary = sgl.getSummary();

        List<String> testFailures = new ArrayList<>();
        
        for (Failure failure : summary.getFailures()) {
        	StringBuilder failureMessage = new StringBuilder();
        	
        	
        	failureMessage.append("Test failed: ");
        	failure.getTestIdentifier().getSource().ifPresentOrElse(
        			s -> {
        				if (s instanceof MethodSource) {
        					MethodSource ms = (MethodSource) s;
        					failureMessage.append(ms.getClassName()).append(".").
        					append(ms.getMethodName()).
        					append("(").append(ms.getMethodParameterTypes()).append(") - ").
        					append(failure.getTestIdentifier().getDisplayName());
        				}
        			}, 
        			() -> failureMessage.append(failure.getTestIdentifier().getDisplayName()));
        	
        	failureMessage.append("\n");
        	
//        	failure.getTestIdentifier().getParentId().ifPresent(p ->
//        		failureMessage.append("in: ").append(p).append("\n"));
        	failureMessage.append("```\n");
        	addThrowable(failure.getException(), failureMessage);
        	failureMessage.append("\n```");
        	
        	testFailures.add(failureMessage.toString());
        }
        return testFailures;
    }

	private void addThrowable(Throwable exception, StringBuilder failureMessage) {
		failureMessage.append(exception.getClass().getName()).append(": ").append(exception.getMessage());
		StackTraceElement[] stes = exception.getStackTrace();
		for (int i = 0; i < Math.min(stes.length, 5); i++) {
			failureMessage.append("\n  at " + stes[i].toString());
		}
		if (stes.length > 5)
			failureMessage.append("\n  ...");
		if (exception.getCause() != null) {
			failureMessage.append("\nCaused by:\n");
			addThrowable(exception.getCause(), failureMessage);
		}
	}

}
