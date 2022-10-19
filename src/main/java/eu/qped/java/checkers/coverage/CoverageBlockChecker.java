package eu.qped.java.checkers.coverage;

import eu.qped.framework.*;
import eu.qped.framework.qf.*;
import eu.qped.java.checkers.coverage.feedback.Formatter;
import eu.qped.java.checkers.coverage.feedback.Summary;
import eu.qped.java.checkers.coverage.feedback.wanted.ParserWF;
import eu.qped.java.checkers.coverage.framework.ast.*;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;
import eu.qped.java.utils.compiler.Com;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Creates a custom code coverage for given list of classes
 * @author Herfurth
 */
public class CoverageBlockChecker implements  CoverageChecker {

    // frameworks
    // defines what frameworks are used
    private static final String COVERAGE_FRAMEWORK = "JACOCO", AST_FRAMEWORK = "JAVA_PARSER", TEST_FRAMEWORK = "JUNIT5";


    QfCovSetting covSetting;
    @QfProperty
    FileInfo file = null;
    @QfProperty
    String privateImplementation = null;
    @QfProperty
    String answer = null;

    public CoverageBlockChecker() {

    }

    public CoverageBlockChecker(QfCovSetting covSetting) {
        this.covSetting = covSetting;
        this.file = covSetting.getFile();
        this.privateImplementation = covSetting.getPrivateImplementation();
        this.answer = covSetting.getAnswer();
    }

    public Summary checker(List<CovInformation> testClasses, List<CovInformation> classes) {
        Summary summary = new Summary();
        try {
            AstFramework ast = AstFrameworkFactoryAbstract.create(AST_FRAMEWORK).create();
            TestFrameworkFactory test = TestFrameworkFactoryAbstract.create(TEST_FRAMEWORK);
            CoverageFramework coverage = CoverageFrameworkFactoryAbstract.create(COVERAGE_FRAMEWORK).create(test);

            summary = (Summary) ast.analyze(
                    summary,
                    new LinkedList<>(classes),
                    covSetting.getExcludeByType(),
                    covSetting.getExcludeByName());

            summary = (Summary) coverage.analyze(
                    summary,
                    new LinkedList<>(testClasses),
                    new LinkedList<>(classes));

            summary.analyze(new ParserWF().parse(covSetting.getLanguage(), covSetting.getFeedback()));
            return summary;
        } catch (Exception e) {
            throw new InternalError(ErrorMSG.UPS, e);
        }
    }

    public String[] check()  {
        CoverageSetup.Data data = new CoverageSetup(covSetting).setUp();
        if (! data.isCompiled) {
            data.cleanUp(); // TODO
            return data.syntaxFeedback.toArray(String[]::new);
        }

        try {
            String[] fb = Formatter.format(
                    covSetting.getFormat(),
                    checker(data.testclasses, data.classes));
            data.cleanUp();
            return fb;
        } catch (Exception e) {
            data.cleanUp();
            return new String[] {e.getMessage()};
        }
    }



}
