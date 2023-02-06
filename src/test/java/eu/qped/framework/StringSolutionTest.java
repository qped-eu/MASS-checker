package eu.qped.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTParser;
import org.junit.jupiter.api.Test;

import eu.qped.framework.QpedQfFilesUtility.AnswerDescription;

class StringSolutionTest {
	
	@Test
	public void testCompilationUnitNoError1() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "class C2 {}\n"
				+ "public class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\");\n"
				+ "		}\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C1", info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testCompilationUnitNoError2() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "class C2 {}\n"
				+ "class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\");\n"
				+ "		}\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C2", info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testCompilationUnitNoError3() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("class C2 {}\n"
				+ "class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\");\n"
				+ "		}\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals("C2", info.getPrimaryTypeSimpleName());
	}
	
	@Test
	public void testCompilationUnitNoError4() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("class C3 {}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals("C3", info.getPrimaryTypeSimpleName());
	}
	
	@Test
	public void testCompilationUnitIllegalStatement1() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "class C2 {}\n"
				+ "public class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\")\n"
				+ "		}\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C1", info.getPrimaryTypeSimpleName());
	}
	
	@Test
	public void testCompilationUnitIllegalMethod() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "class C2 {}\n"
				+ "public class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\");\n"
				+ "		\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C1", info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testCompilationUnitIllegalMember() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "class C2 {\n"
				+ "		public\n"
				+ "}\n"
				+ "public class C1 {\n"
				+ "		public static void main(String[] args) {\n"
				+ "				System.out.println(\"Hello World\");\n"
				+ "		}\n"
				+ "		class Inner {}\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C1", info.getPrimaryTypeSimpleName());
	}

	
	@Test
	public void testCompilationUnitIllegalClass() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("package pck;\n"
				+ "import java.util.*;\n"
				+ "public class C1");
		assertNotNull(info);
		assertEquals(ASTParser.K_COMPILATION_UNIT, info.getKind());
		assertEquals("pck", info.getPackageName());
		assertEquals("C1", info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testMethodsNoError() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("void m1() {}\n"
				+ "int m2() {return 0;}");
		assertNotNull(info);
		assertEquals(ASTParser.K_CLASS_BODY_DECLARATIONS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testMethodsErrorInBody() {
		 String code = "void rec (){\n"
	                + "System.out.println(\"pretty\")\n"
	                + "}";
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription(code);
		assertNotNull(info);
		assertEquals(ASTParser.K_CLASS_BODY_DECLARATIONS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testStatementsNoError1() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("System.out.println();\n"
				+ "int i = 0;\n"
				+ "System.out.println(i);");
		assertNotNull(info);
		assertEquals(ASTParser.K_STATEMENTS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testStatementsNoError2() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("int i = 0;\n"
				+ "System.out.println(i);");
		assertNotNull(info);
		assertEquals(ASTParser.K_STATEMENTS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}
	
	@Test
	public void testStatementsMissingSemicolon() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("int i = 0\n"
				+ "System.out.println(i);");
		assertNotNull(info);
		assertEquals(ASTParser.K_STATEMENTS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testStatementsIllegalStatement() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("int(true) {\n"
				+ "		x = x + 1;\n"
				+ "} else {\n"
				+ "}");
		assertNotNull(info);
		assertEquals(ASTParser.K_STATEMENTS, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testExpression() {
		AnswerDescription info = QpedQfFilesUtility.getAnswerDescription("((7 + 2 * i) > 10) ? \"\" : \"BLUB\"");
		assertNotNull(info);
		assertEquals(ASTParser.K_EXPRESSION, info.getKind());
		assertEquals("", info.getPackageName());
		assertEquals(null, info.getPrimaryTypeSimpleName());
	}

	@Test
	public void testWriteCompilationUnit() throws IOException {
		File testOutput = new File("target" + File.separator + "test-output" + File.separator + "compilation-unit");
		QpedQfFilesUtility.createFileFromAnswerString(testOutput,
		"package pck;\n"
		+ "import java.util.*;\n"
		+ "class C2 {}\n"
		+ "public class Ä {\n"
		+ "		public static void main(String[] args) {\n"
		+ "				System.out.println(\"Hello World\");\n"
		+ "		}\n"
		+ "		class Inner {}\n"
		+ "}");
	}

	@Test
	public void testWriteMethod() throws IOException {
		File testOutput = new File("target" + File.separator + "test-output" + File.separator + "methods");
		QpedQfFilesUtility.createFileFromAnswerString(testOutput,
		"public final void m() {\n"
		+ "		int i = 0;\n"
		+ "		System.out.println(i);\n"
		+ "}");
	}
	
	@Test
	public void testWriteStatements() throws IOException {
		File testOutput = new File("target" + File.separator + "test-output" + File.separator + "statements");
		QpedQfFilesUtility.createFileFromAnswerString(testOutput,
		"int i = 1;\n"
		+ "i = i + 1;\n"
		+ "double d = Math.pow(1.0, 2.0);");
	}

	@Test
	public void testWriteExpression() throws IOException {
		File testOutput = new File("target" + File.separator + "test-output" + File.separator + "expression");
		QpedQfFilesUtility.createFileFromAnswerString(testOutput,
		"Math.pow(1.0, 2.0)");
	}

}
