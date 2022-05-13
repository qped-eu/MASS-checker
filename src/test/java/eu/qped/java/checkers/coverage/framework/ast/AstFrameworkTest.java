package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;
import eu.qped.java.checkers.coverage.testhelp.FileResources;
import eu.qped.java.checkers.coverage.testhelp.Preprocessed;
import eu.qped.java.checkers.coverage.testhelp.Preprocessing;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AstFrameworkTest {
    private class Want {
        final StatementType type;
        final String method;
        final int start;
        final int end;

        public Want(StatementType type, String method, int start, int end) {
            this.type = type;
            this.method = method;
            this.start = start;
            this.end = end;
        }
    }

    private class Results implements AstCollection{
        LinkedList<AstResult> results = new LinkedList<>();
        Results() {}

        @Override
        public void add(AstResult result) {
            results.add(result);
        }
    }

    @Test
    public void analyzeConstructor() {
        List<Want> wants = Arrays.asList(
                new Want(StatementType.CONSTRUCTOR, "Constructor", 6, 8),
                new Want(StatementType.CONSTRUCTOR, "Constructor", 10, 12),
                new Want(StatementType.METHOD, "add", 14,  16));
        genericTest(List.of("test.Constructor"), wants, "JAVA_PARSER");
    }

    @Test
    public void analyzeLoopStmt() {
        List<Want> wants = Arrays.asList(
                new Want(StatementType.METHOD, "forStmt", 7, 13),
                new Want(StatementType.FOR, "forStmt", 9, 11),
                new Want(StatementType.METHOD, "foreachStmt", 15,  21),
                new Want(StatementType.FOREACH, "foreachStmt", 17, 19),
                new Want(StatementType.METHOD, "whileStmt", 23,  31),
                new Want(StatementType.WHILE, "whileStmt", 25, 28));
        genericTest(List.of("test.LoopStmt"), wants, "JAVA_PARSER");
    }

    @Test
    public void analyzeCaseStmt() {
        List<Want> wants = Arrays.asList(
                new Want(StatementType.METHOD, "caseStmt", 5,15),
                new Want(StatementType.CASE, "caseStmt", 7,9),
                new Want(StatementType.CASE, "caseStmt", 10,12),

                new Want(StatementType.METHOD, "caseDefaultStmt", 17,30),
                new Want(StatementType.CASE, "caseDefaultStmt", 19,21),
                new Want(StatementType.CASE, "caseDefaultStmt", 22,24),
                new Want(StatementType.CASE, "caseDefaultStmt", 25,27),

                new Want(StatementType.METHOD, "nestedCaseStmt", 33,49),
                new Want(StatementType.CASE, "nestedCaseStmt", 35,37),
                new Want(StatementType.CASE, "nestedCaseStmt", 38,46),
                new Want(StatementType.CASE, "nestedCaseStmt", 40,42),
                new Want(StatementType.CASE, "nestedCaseStmt", 43,45));

        genericTest(List.of("test.CaseStmt"), wants, "JAVA_PARSER");
    }

    @Test
    public void analyzeIfStmt() {
        List<Want> wants = Arrays.asList(
                new Want(StatementType.METHOD, "ifStmt", 5,10),
                new Want(StatementType.IF,"ifStmt", 6, 8),

                new Want(StatementType.METHOD, "ifElseStmt", 13,19),
                new Want(StatementType.IF,"ifElseStmt", 14, 15),
                new Want(StatementType.ELSE,"ifElseStmt", 16, 18),

                new Want(StatementType.METHOD, "nestedIfStmts", 22,41),
                new Want(StatementType.IF,"nestedIfStmts", 23, 26),
                new Want(StatementType.IF,"nestedIfStmts", 24, 26),
                new Want(StatementType.ELSE_IF,"nestedIfStmts", 27, 32),
                new Want(StatementType.IF,"nestedIfStmts", 28, 29),
                new Want(StatementType.ELSE,"nestedIfStmts", 30, 32),
                new Want(StatementType.ELSE,"nestedIfStmts", 33, 39),
                new Want(StatementType.IF,"nestedIfStmts", 34, 35),
                new Want(StatementType.ELSE_IF,"nestedIfStmts", 36, 38),

                new Want(StatementType.METHOD, "ifNoBody", 44,47),
                new Want(StatementType.IF,"ifNoBody", 45, 45));
        genericTest(List.of("test.IfStmt"), wants, "JAVA_PARSER");
    }

    private void genericTest(List<String> classes, List<Want> wants, String framework) {
        Preprocessed preprocessed = new Preprocessing().processingOnlyContent(
                FileResources.filesByClassName,
                FileResources.convertNames,
                new LinkedList<>(),
                classes );
        Results gots = new Results();
        AstFramework toTest = AstFrameworkFactoryAbstract.create(framework).create();
        gots = (Results) toTest.analyze(gots, new LinkedList<>(preprocessed.getClasses()), new HashSet<>(), new HashSet<>());

        if (false)
            gots.results.forEach(a -> System.out.println(a.methodName + " " + a.type + " " + a.start + " " + a.end ));

        assertEquals(wants.size(), gots.results.size());
        Iterator<AstResult> iterator = gots.results.iterator();
        int i = 0;
        for (Want w : wants) {
            AstResult g = iterator.next();
            assertEquals(w.start, g.start, "at index " + i);
            assertEquals(w.end, g.end,"at index " + i);
            assertEquals(w.type, g.type,"at index " + i);
            assertEquals(w.method, g.methodName,"at index " + i++);
        }
    }
}