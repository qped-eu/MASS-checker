package eu.qped.java.checkers.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.qped.framework.checker.Checker;
import eu.qped.framework.qf.QFMainSettings;
import eu.qped.framework.qf.QFSemSettings;
import eu.qped.framework.qf.QFStyleSettings;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticConfigurator;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleConfigurator;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxErrorChecker;
import eu.qped.java.checkers.syntax.SyntaxFeedback;


public class Mass implements Checker {
    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        Map<String, String> styleSettings = new HashMap<>();
        Map<String , String> mainSettings = new HashMap<>();

        QFStyleSettings qfStyleSettings = qfObject.getQfStyleConf();


        qfObject.setSettings(qfObject.getSettings());
        QFSemSettings qfSemSettings = qfObject.getQfSemConfigs();
        /*
        main settings
         */
        QFMainSettings qfMainSettings = qfObject.getQfMainSettings();
        mainSettings.put("syntaxLevel" , qfMainSettings.getSyntaxLevel());
        mainSettings.put("preferredLanguage" , qfMainSettings.getPreferredLanguage());
        mainSettings.put("styleNeeded" , qfMainSettings.getStyleNeeded());
        mainSettings.put("semanticNeeded" , qfMainSettings.getSemanticNeeded());

        /*
        Style Configs
         */
        styleSettings.put("mainLevel" , qfStyleSettings.getMainLevel());
        styleSettings.put("maxClassLength" , qfStyleSettings.getClassLength());
        styleSettings.put("maxMethodLength", qfStyleSettings.getMethodLength());
        styleSettings.put("maxFieldsCount", qfStyleSettings.getFieldsCount());
        styleSettings.put("maxCycloComplexity", qfStyleSettings.getCycloComplexity());
        styleSettings.put("varNamesRegEx", qfStyleSettings.getVarName());
        styleSettings.put("methodNamesRegEx", qfStyleSettings.getMethodName());
        styleSettings.put("classNameRegEx", qfStyleSettings.getClassName());
        styleSettings.put("basisLevel", qfStyleSettings.getBasisLevel());
        styleSettings.put("namesLevel", qfStyleSettings.getNamesLevel());
        styleSettings.put("compLevel", qfStyleSettings.getCompLevel());

        StyleConfigurator styleConfigurator = StyleConfigurator.createStyleConfigurator(qfStyleSettings);

        StyleChecker styleChecker = new StyleChecker(styleConfigurator);




        Map<String , String> semanticSettings = new HashMap<>();

        /*
        Semantic Configs
         */

        semanticSettings.put("methodName" , qfSemSettings.getMethodName());
        semanticSettings.put("recursionAllowed" , qfSemSettings.getRecursionAllowed());
        semanticSettings.put("whileLoop" , qfSemSettings.getWhileLoop());
        semanticSettings.put("forLoop" , qfSemSettings.getForLoop());
        semanticSettings.put("forEachLoop" , qfSemSettings.getForEachLoop());
        semanticSettings.put("ifElseStmt" , qfSemSettings.getIfElseStmt());
        semanticSettings.put("doWhileLoop" , qfSemSettings.getDoWhileLoop());
        semanticSettings.put("returnType" , qfSemSettings.getReturnType());

        MainSettings mainSettingsConfiguratorConf = new MainSettings(mainSettings);
        SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(new QFSemSettings());




        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);
        SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(qfObject.getAnswer());

        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxErrorChecker, mainSettingsConfiguratorConf);

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


        String[] result = new String[styleFeedbacks.size() + semanticFeedbacks.size() + syntaxFeedbacks.size() + 100];

        int i = 0;

        for (StyleFeedback styleFeedback : styleFeedbacks){
            result[i] = "style Feedback";
            result[i+1] = styleFeedback.getDesc()
                    + NEW_LINE
                    +styleFeedback.getBody()
                    +NEW_LINE
                    +styleFeedback.getLine()
                    +NEW_LINE
                    +styleFeedback.getExample()
                    + NEW_LINE
                    +"------------------------------------------------------------------------------";
            i = i+2;
        }

        for (SemanticFeedback semanticFeedback : semanticFeedbacks){
            result[i] = "semantic Feedback";
            result[i+1] = semanticFeedback.getBody() + NEW_LINE
                    +"--------------------------------------------------";
            i = i + 2 ;
        }

        for (SyntaxFeedback syntax: syntaxFeedbacks){
            result[i+1] = syntax.getHead() + NEW_LINE
            + syntax.getBody() + NEW_LINE +
                    syntax.getExample() + NEW_LINE
                    +"--------------------------------------------------";
            i = i+2;
        }

        qfObject.setFeedback(result);
    }
}
