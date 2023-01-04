package eu.qped.java.utils;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.analyser.SyntaxAnalysisReport;
import eu.qped.java.checkers.syntax.analyser.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;


public class CompilerTest {

    @Test
    public void compileStringTest() throws IOException{
    	
        String erroneousMethod = "private void print() {\n" +
                "        System.out.println()\n" +
                "    }";

        Assertions.assertFalse(compile(erroneousMethod).isCompilable());

        String correctMethod = "private void print() {\n" +
                "        System.out.println();\n" +
                "    }";

        Assertions.assertTrue(compile(correctMethod).isCompilable());

    }

	public SyntaxAnalysisReport compile(String answer) throws IOException {
		SyntaxSetting syntaxSetting = SyntaxSetting.builder().build();
        syntaxSetting.setLanguage(SupportedLanguages.ENGLISH);
        syntaxSetting.setCheckLevel(CheckLevel.BEGINNER);

        File solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, answer);
        
        SyntaxErrorAnalyser syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .solutionRoot(solutionRoot)
                    .build();
        SyntaxAnalysisReport analyseReport = syntaxErrorAnalyser.check();

        SyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
        syntaxFeedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
		return analyseReport;
	}

    @Test
    public void compileProjectFalseTest() throws IOException {

        String falseCode = "class test {private void print() { \n System.out.println() \n  }}";
        
        SyntaxAnalysisReport compilationResult = compile(falseCode);

        Assertions.assertFalse(compilationResult.isCompilable());
        Assertions.assertEquals(1, compilationResult.getSyntaxErrors().size());
    }

    @Test
    public void compileProjectTrueTest() throws IOException {
        String correctCode = "class test {private void print() { \n System.out.println(); \n  }}";

        SyntaxAnalysisReport compilationResult = compile(correctCode);

        Assertions.assertTrue(compilationResult.isCompilable());
        Assertions.assertEquals(0, compilationResult.getSyntaxErrors().size());
    }

    @Test
    public void compileProjectWithoutFilesTest() throws IOException {
        String emptyAnswer = "";

        SyntaxAnalysisReport compilationResult = compile(emptyAnswer);

        Assertions.assertTrue(compilationResult.isCompilable());
        Assertions.assertEquals(0, compilationResult.getSyntaxErrors().size());
    }


}
