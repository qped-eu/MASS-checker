package eu.qped.java.checkers.coverage;

import eu.qped.framework.*;
import eu.qped.framework.qf.*;
import eu.qped.java.checkers.coverage.feedback.Summary;
import eu.qped.java.checkers.coverage.feedback.wanted.ParserWF;
import eu.qped.java.checkers.coverage.framework.ast.*;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;
import java.util.*;

/**
 * Creates a custom code coverage for given list of classes
 * @author Herfurth
 */
public class CoverageChecker implements Checker {
    @QfProperty
    QfCovSetting covSetting;
    @QfProperty
    QfUser user;
    @QfProperty
    FileInfo file;
    @QfProperty
    FileInfo additional;

    @Override
    public void check(QfObject qfObject) throws Exception {
        // TODO
    }

    public Summary checker(
            List<CovInformation> testClasses,
            List<CovInformation> classes) {
        Summary summary = new Summary();
        try {
            AstFramework ast = AstFrameworkFactoryAbstract.create("JAVA_PARSER").create();
            TestFrameworkFactory test = TestFrameworkFactoryAbstract.create("JUNIT5");
            CoverageFramework coverage = CoverageFrameworkFactoryAbstract.create("JACOCO").create(test);

            summary = (Summary) ast.analyze(
                    summary,
                    new LinkedList<>(classes),
                    covSetting.getExcludeByTypeSet(),
                    covSetting.getExcludeByNameSet());

            summary = (Summary) coverage.analyze(
                    summary,
                    new LinkedList<>(testClasses),
                    new LinkedList<>(classes));

            summary.analyze(new ParserWF().parse(user.getLanguage(), covSetting.getFeedback()));
        } catch (Exception e) {
            e.printStackTrace();
            return summary;
        }
        return summary;
    }

}
