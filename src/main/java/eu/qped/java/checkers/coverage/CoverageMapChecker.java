package eu.qped.java.checkers.coverage;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.feedback.SummaryMapped;
import eu.qped.java.checkers.coverage.feedback.wantMap.Parser;
import eu.qped.java.checkers.coverage.feedback.wantMap.Provider;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFramework;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFrameworkFactoryAbstract;
import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactory;
import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactoryAbstract;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageMapChecker implements CoverageChecker {
    // frameworks
    // defines what frameworks are used
    private static final String COVERAGE_FRAMEWORK = "JACOCO", TEST_FRAMEWORK = "JUNIT5";
    private final QfCovSetting setting;
    public CoverageMapChecker(QfCovSetting setting) {
        this.setting = setting;
    }

    @Override
    public String[] check() {
        CoverageSetup.Data data = setting.getData();
        data.cleanUp();
        if (! data.isCompiled) {
            return data.syntaxFeedback.toArray(String[]::new);
        }

        return checker(data.testclasses, data.classes);
    }

    public String[] checker(List<CovInformation> testClasses, List<CovInformation> classes) {
        SummaryMapped summary = new SummaryMapped();
        try {
            TestFrameworkFactory test = TestFrameworkFactoryAbstract.create(TEST_FRAMEWORK);
            CoverageFramework coverage = CoverageFrameworkFactoryAbstract.create(COVERAGE_FRAMEWORK).create(test);

            coverage.analyze(
                    summary,
                    new LinkedList<>(testClasses),
                    new LinkedList<>(classes));

            summary.analyse(new Parser().parse(setting.getLanguage(), setting.getFeedback()));

            List<Feedback> fb = new LinkedList<>();
            fb.addAll(summary.testFBs());
            fb.addAll(summary.classFBs().stream().flatMap(c -> c.feedbacks().stream()).collect(Collectors.toList()));

            return fb.stream().map(Feedback::getBody).map(s -> String.format("---\n\n%s\n\n",s)).toArray(String[]::new);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{ErrorMSG.UPS};
        }
    }



}
