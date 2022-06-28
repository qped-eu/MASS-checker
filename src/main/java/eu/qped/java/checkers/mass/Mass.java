package eu.qped.java.checkers.mass;

import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.coverage.QfCovSetting;
import eu.qped.java.checkers.design.DesignChecker;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticConfigurator;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.feedback.syntax.SyntaxFeedback;

import java.util.*;


public class Mass implements Checker {

    @QfProperty
    private QFMainSettings mainSettings;

    @QfProperty
    private QFStyleSettings styleSettings;

    @QfProperty
    private QFSemSettings semSettings;

    @QfProperty
    private QFClassSettings classSettings;

    @QfProperty
    private QfCovSetting covSetting;

    @QfProperty
    private FileInfo file;  // unzipped

    @QfProperty
    private FileInfo additional; // not unzipped


    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        // Main Settings
        MainSettings mainSettings = new MainSettings(this.mainSettings);

        // Syntax Checker
        SyntaxChecker syntaxChecker;
        if (Objects.nonNull(file) && Objects.nonNull(file.getUnzipped())) {
            syntaxChecker = SyntaxChecker.builder().stringAnswer(qfObject.getAnswer()).targetProject(file.getUnzipped().getAbsolutePath()).build();
        } else {
            syntaxChecker = SyntaxChecker.builder().stringAnswer(qfObject.getAnswer()).build();
        }

        // Style Checker

        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(styleSettings).build();

        SemanticChecker semanticChecker = null;
        if (Objects.nonNull(semSettings)) {
            SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(semSettings);
            semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);
        }
        // Design Checker
        DesignChecker designChecker = DesignChecker.builder().build(); //TODO is this correct?

        //Class Checker

        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(this.classSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);


        // CoverageChecher
        CoverageChecker coverageChecker = null;
        if (Objects.nonNull(covSetting)) {
            covSetting.setFile(file);
            covSetting.setAdditional(additional);
            covSetting.setLanguage(mainSettings.getPreferredLanguage());
            covSetting.setAnswer(qfObject.getAnswer());
            coverageChecker = new CoverageChecker(covSetting);
        }


        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, designChecker, classChecker, mainSettings, coverageChecker);
        massExecutor.execute();

        /*
         feedbacks
         */
        List<StyleFeedback> styleFeedbacks;
        styleFeedbacks = massExecutor.getStyleFeedbacks();


        List<SyntaxFeedback> syntaxFeedbacks;
        syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();

        List<SemanticFeedback> semanticFeedbacks;
        semanticFeedbacks = massExecutor.getSemanticFeedbacks();

        List<ClassFeedback> classFeedbacks;
        classFeedbacks = massExecutor.getClassFeedbacks();

        List<String> coverageFeedback = massExecutor.getCoverageFeedbacks();


        String[] result = new String[2* (styleFeedbacks.size() + semanticFeedbacks.size()  + syntaxFeedbacks.size()  + classFeedbacks.size()) + coverageFeedback.size()]; // +100 ????

        int i = 0;

        for (StyleFeedback styleFeedback : styleFeedbacks) {
            result[i] = "style Feedback";
            result[i + 1] = styleFeedback.getDesc()
                    + NEW_LINE
                    + styleFeedback.getContent()
                    + NEW_LINE
                    + styleFeedback.getLine()
                    + NEW_LINE
                    + styleFeedback.getExample()
                    + NEW_LINE
                    + "------------------------------------------------------------------------------";
            i = i + 2;
        }

        for (SemanticFeedback semanticFeedback : semanticFeedbacks) {
        result[i] = "semantic Feedback";
        result[i + 1] = semanticFeedback.getBody() + NEW_LINE
                + "--------------------------------------------------";
        i = i + 2;
        }

        for (ClassFeedback classFeedback : classFeedbacks) {
            result[i] = "class Feedback";
            result[i + 1] = classFeedback.getBody() + NEW_LINE
                    + "--------------------------------------------------";
            i = i + 2;
        }

        for (SyntaxFeedback syntax : syntaxFeedbacks) {
            result[i] = "Syntax Feedback";
            result[i + 1] = ""
                + syntax.toString()
                + NEW_LINE
                + "--------------------------------------------------";
            i = i + 2;
        }

        for (Iterator<String> ifeedback = coverageFeedback.iterator(); ifeedback.hasNext(); ) {
            if (i >= result.length) {
                break;
            }
            result[i++] = ifeedback.next();
        }

        qfObject.setFeedback(result);
}

}