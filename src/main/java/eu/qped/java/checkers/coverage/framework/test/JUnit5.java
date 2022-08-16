package eu.qped.java.checkers.coverage.framework.test;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.*;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

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

        if (summary.getTotalFailureCount() == 0)
        	return Collections.emptyList();
        
        try (StringWriter sw = new StringWriter()) {
        	try (PrintWriter pw = new PrintWriter(sw)) {
            	summary.printFailuresTo(pw, 0);
        		pw.flush();
        		sw.flush();
        		return Collections.singletonList(sw.toString());
        	}
        }
        catch (Exception e) {
        	throw new RuntimeException(e);
        }
    }

}
