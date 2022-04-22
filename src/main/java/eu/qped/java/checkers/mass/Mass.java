package eu.qped.java.checkers.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.qped.framework.Checker;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticConfigurator;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleConfigurator;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxFeedback;


public class Mass implements Checker {
	
	@QfProperty
	private QFMainSettings mainSettings;
	
	@QfProperty
	private QFStyleSettings styleSettings;

	@QfProperty
	private QFSemSettings semSettings;

    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        Map<String, String> styleSettings = new HashMap<>();
        Map<String , String> mainSettings = new HashMap<>();


        /*
        main settings
         */
        QFMainSettings qfMainSettings = this.mainSettings;
        mainSettings.put("syntaxLevel" , qfMainSettings.getSyntaxLevel());
        mainSettings.put("preferredLanguage" , qfMainSettings.getPreferredLanguage());
        mainSettings.put("styleNeeded" , qfMainSettings.getStyleNeeded());
        mainSettings.put("semanticNeeded" , qfMainSettings.getSemanticNeeded());

        /*
        Style Configs
         */
        styleSettings.put("mainLevel" , this.styleSettings.getMainLevel());
        styleSettings.put("maxClassLength" , this.styleSettings.getClassLength());
        styleSettings.put("maxMethodLength", this.styleSettings.getMethodLength());
        styleSettings.put("maxFieldsCount", this.styleSettings.getFieldsCount());
        styleSettings.put("maxCycloComplexity", this.styleSettings.getCycloComplexity());
        styleSettings.put("varNamesRegEx", this.styleSettings.getVarName());
        styleSettings.put("methodNamesRegEx", this.styleSettings.getMethodName());
        styleSettings.put("classNameRegEx", this.styleSettings.getClassName());
        styleSettings.put("basisLevel", this.styleSettings.getBasisLevel());
        styleSettings.put("namesLevel", this.styleSettings.getNamesLevel());
        styleSettings.put("compLevel", this.styleSettings.getCompLevel());

        StyleConfigurator styleConfigurator = StyleConfigurator.createStyleConfigurator(this.styleSettings);

        StyleChecker styleChecker = new StyleChecker(styleConfigurator);




        Map<String , String> semanticSettings = new HashMap<>();

        /*
        Semantic Configs
         */

        semanticSettings.put("methodName" , this.semSettings.getMethodName());
        semanticSettings.put("recursionAllowed" , this.semSettings.getRecursionAllowed());
        semanticSettings.put("whileLoop" , this.semSettings.getWhileLoop());
        semanticSettings.put("forLoop" , this.semSettings.getForLoop());
        semanticSettings.put("forEachLoop" , this.semSettings.getForEachLoop());
        semanticSettings.put("ifElseStmt" , this.semSettings.getIfElseStmt());
        semanticSettings.put("doWhileLoop" , this.semSettings.getDoWhileLoop());
        semanticSettings.put("returnType" , this.semSettings.getReturnType());

        MainSettings mainSettingsConfiguratorConf = new MainSettings(mainSettings);
        SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(new QFSemSettings());




        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().answer(qfObject.getAnswer()).build();

        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, mainSettingsConfiguratorConf);

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
            result[i+1] = syntax.getFeedbackContent() + NEW_LINE
            + syntax.getBody() + NEW_LINE +
                    syntax.getSolutionExample() + NEW_LINE
                    +"--------------------------------------------------";
            i = i+2;
        }

        qfObject.setFeedback(result);
    }

}
