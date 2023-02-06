package eu.qped.java.checkers.solutionapproach.analyser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.template.TemplateBuilder;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.configs.SemanticSettingItem;
import eu.qped.java.checkers.solutionapproach.configs.SemanticSettingReader;
import eu.qped.java.checkers.solutionapproach.configs.SolutionApproachGeneralSettings;
import eu.qped.java.checkers.solutionapproach.configs.SolutionApproachReportItem;
import eu.qped.java.utils.SupportedLanguages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SolutionApproachAnalyser {

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private QfSemanticSettings qfSemanticSettings;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private SemanticSettingReader semanticSettingReader;

    private String targetProjectPath;


    public List<SolutionApproachReportItem> check() {

        List<SolutionApproachReportItem> solutionApproachEntries = new ArrayList<>();

        SemanticSettingReader reader = SemanticSettingReader.builder().qfSemanticSettings(qfSemanticSettings).build();

        var settings = reader.groupByFileName();
        // per File
        settings.forEach((filePath, semanticSettingItems) -> {
            String path = getRelatedPath(filePath);
            var compilationUnit = parse(path); // AST per File
            // AST per Method in File
            semanticSettingItems.forEach(
                    semanticSettingItem -> {
                        try {
                            // analytics phase
                            SolutionApproachReportItem basicReportEntry = SolutionApproachReportItem.builder().relatedSemanticSettingItem(semanticSettingItem).build();
                            BlockStmt targetedMethod = getTargetedMethod(compilationUnit, basicReportEntry); // get target method and the return type of the target method
                            StatementsVisitorAnalyser statementsVisitorHelper = StatementsVisitorAnalyser.createStatementsVisitorHelper(targetedMethod);
                            MethodCalledAnalyser recursiveCheckHelper = MethodCalledAnalyser.createRecursiveCheckHelper(targetedMethod);
                            updateReportEntryFields(basicReportEntry, statementsVisitorHelper, recursiveCheckHelper); // update basic Report Entry
                            // create report Entries from basic report Entry
                            solutionApproachEntries.addAll(generateLoopReportEntries(basicReportEntry));
                            solutionApproachEntries.addAll(generateRecursionReportEntries(basicReportEntry));
                            solutionApproachEntries.addAll(generateReturnTypeReportEntry(basicReportEntry));
                        } catch (NoSuchMethodException e) {
                            System.out.println(e.getMessage() + " " + e.getCause());
                        }
                    }
            );
        });
        return solutionApproachEntries;
    }

    private String getRelatedPath(String filePath) {
        if (targetProjectPath == null) {
            targetProjectPath = "";
        }
        var path = "";
        if (filePath != null && !"".equals(filePath)) {
            if ('/' == filePath.charAt(0)) {
                path = targetProjectPath + filePath;
            } else {
                if (!Objects.equals(targetProjectPath, "")) {
                    path = targetProjectPath + "/" + filePath;
                } else {
                    path = filePath;
                }
            }
        } else path = targetProjectPath;  // answer is string
        return path;
    }


    private List<SolutionApproachReportItem> generateReturnTypeReportEntry(SolutionApproachReportItem basicReportEntry) {
        List<SolutionApproachReportItem> result = new ArrayList<>();
        var semanticSettingItem = basicReportEntry.getRelatedSemanticSettingItem();
        if (!semanticSettingItem.getReturnType().equalsIgnoreCase(basicReportEntry.getSolutionReturnType()) && !semanticSettingItem.getReturnType().equals("undefined")) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("differentReturnTypeThanExpected");
            result.add(reportEntry);
        }
        return result;
    }

    private List<SolutionApproachReportItem> generateRecursionReportEntries(SolutionApproachReportItem basicReportEntry) {
        List<SolutionApproachReportItem> result = new ArrayList<>();
        var semanticSettingItem = basicReportEntry.getRelatedSemanticSettingItem();
        if (semanticSettingItem.getRecursive() && !basicReportEntry.isSolutionHasRecursiveMethodCall() && !basicReportEntry.isSolutionHasLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("solutionMustHaveRecursion");
            result.add(reportEntry);
        } else if (semanticSettingItem.getRecursive() && !basicReportEntry.isSolutionHasRecursiveMethodCall() && basicReportEntry.isSolutionHasLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("solutionMustHaveRecursionInsteadLoop");
            result.add(reportEntry);
        } else if (!semanticSettingItem.getRecursive() && basicReportEntry.isSolutionHasRecursiveMethodCall()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("solutionMustNotHaveRecursion");
            result.add(reportEntry);
        }
        return result;
    }

    private List<SolutionApproachReportItem> generateLoopReportEntries(SolutionApproachReportItem basicReportEntry) {
        List<SolutionApproachReportItem> result = new ArrayList<>();
        var semanticSettingItem = basicReportEntry.getRelatedSemanticSettingItem();
        if (semanticSettingItem.getWhileLoop() != -1 && basicReportEntry.getSolutionWhileCounter() > semanticSettingItem.getWhileLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("moreThenExpectedWhileLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getForLoop() != -1 && basicReportEntry.getSolutionForCounter() > semanticSettingItem.getForLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("moreThenExpectedForLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getForEachLoop() != -1 && basicReportEntry.getSolutionForEachCounter() > semanticSettingItem.getForEachLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("moreThenExpectedForEachLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getIfElseStmt() != -1 && basicReportEntry.getSolutionIfElseCounter() > semanticSettingItem.getIfElseStmt()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("moreThenExpectedIfElseStatement");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getDoWhileLoop() != -1 && basicReportEntry.getSolutionDoWhileCounter() > semanticSettingItem.getDoWhileLoop()) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("moreThenExpectedDoWhileLoops");
            result.add(reportEntry);
        }
        return result;
    }

    private SolutionApproachReportItem cloneBasicReportEntry(SolutionApproachReportItem basicReportEntry) {
        return SolutionApproachReportItem.builder()
                .relatedSemanticSettingItem(basicReportEntry.getRelatedSemanticSettingItem())
                .solutionForCounter(basicReportEntry.getSolutionForCounter())
                .solutionForEachCounter(basicReportEntry.getSolutionForEachCounter())
                .solutionDoWhileCounter(basicReportEntry.getSolutionDoWhileCounter())
                .solutionWhileCounter(basicReportEntry.getSolutionWhileCounter())
                .solutionIfElseCounter(basicReportEntry.getSolutionIfElseCounter())
                .solutionHasLoop(basicReportEntry.isSolutionHasLoop())
                .solutionHasRecursiveMethodCall(basicReportEntry.isSolutionHasRecursiveMethodCall())
                .solutionReturnType(basicReportEntry.getSolutionReturnType())
                .build();
    }

    private void updateReportEntryFields(SolutionApproachReportItem solutionApproachReportItem, StatementsVisitorAnalyser statementsVisitorAnalyser, MethodCalledAnalyser recursiveCheckHelper) {
        solutionApproachReportItem.setSolutionHasRecursiveMethodCall(recursiveCheckHelper.check(solutionApproachReportItem.getRelatedSemanticSettingItem().getMethodName()));
        solutionApproachReportItem.setSolutionForCounter(statementsVisitorAnalyser.getForCounter());
        solutionApproachReportItem.setSolutionForEachCounter(statementsVisitorAnalyser.getForEachCounter());
        solutionApproachReportItem.setSolutionDoWhileCounter(statementsVisitorAnalyser.getDoCounter());
        solutionApproachReportItem.setSolutionWhileCounter(statementsVisitorAnalyser.getWhileCounter());
        solutionApproachReportItem.setSolutionIfElseCounter(statementsVisitorAnalyser.getIfElseCounter());
        solutionApproachReportItem.setSolutionHasLoop(
                solutionApproachReportItem.getSolutionWhileCounter() > 0
                        || solutionApproachReportItem.getSolutionDoWhileCounter() > 0
                        || statementsVisitorAnalyser.getForCounter() > 0
                        || statementsVisitorAnalyser.getForEachCounter() > 0
        );

    }

    private CompilationUnit parse(final String path) {
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser javaParser = new JavaParser(configuration);
        try {
            var unit = javaParser.parse(Path.of(path));
            if (unit.getResult().isPresent()) {
                return unit.getResult().get();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Couldn't parse the given filepath %s", path));
        }
    }


    private BlockStmt getTargetedMethod(CompilationUnit compilationUnit, SolutionApproachReportItem basicReportEntry) throws NoSuchMethodException {
        final BlockStmt[] result = {new BlockStmt()};
        MutableBoolean methodFound = new MutableBoolean(false);

        for (int i = 0; i < compilationUnit.getChildNodes().size(); i++) {
            /*
            all Nodes from SourceFile (Comments, Imports, Class components)
             */
            Node node = compilationUnit.getChildNodes().get(i);
            /*
            every Class component's components;
             */
            if (node.getChildNodes() != null) {
                for (int j = 0; j < node.getChildNodes().size(); j++) {

                    Node currentNode = node.getChildNodes().get(j);

                    currentNode.accept(new VoidVisitorWithDefaults<Void>() {
                        @Override
                        public void visit(MethodDeclaration n, Void arg) {
                            if (n.getName().toString().equals(basicReportEntry.getRelatedSemanticSettingItem().getMethodName())) {
                                basicReportEntry.setSolutionReturnType(n.getType().toString());
                                methodFound.setTrue();
                                if (n.getBody().isPresent()) {
                                    result[0] = n.getBody().get().asBlockStmt();
                                }
                            }

                        }
                    }, null);
                }
            }
        }
        if (!methodFound.getValue()) {
            throw new NoSuchMethodException(NoSuchMethodException.class.getName() + " the Method: " + basicReportEntry.getRelatedSemanticSettingItem().getMethodName() + " not found!");
        }
        return result[0];
    }


    public static void main(String[] args) throws IOException {
        var qfSemanticSetting = QfSemanticSettings
                .builder()
                .semantics(
                        List.of(
                                SemanticSettingItem
                                        .builder()
                                        .recursive(true)
                                        .whileLoop(0)
                                        .forLoop(0)
                                        .forEachLoop(0)
                                        .doWhileLoop(0)
                                        .ifElseStmt(-1)
                                        .returnType("int")
                                        .methodName("calcSum")
                                        .filePath("src/test/resources/code-example-for-sematnic-testing-fail/CalcSum.java")
                                        .build()
                        )
                )
                .build();
        var solutionGeneralSetting = SolutionApproachGeneralSettings.builder()
                .checkLevel(CheckLevel.BEGINNER)
                .build();
        solutionGeneralSetting.setLanguage(SupportedLanguages.ENGLISH);

        SolutionApproachChecker solutionApproachChecker = SolutionApproachChecker.builder()
                .qfSemanticSettings(qfSemanticSetting)
                .solutionApproachGeneralSettings(solutionGeneralSetting)
                .build();

        TemplateBuilder templateBuilder = TemplateBuilder.builder().build();
        var result = solutionApproachChecker.check();
        result.forEach(e -> System.out.println(e));
//        solutionApproachChecker.check().forEach(e -> System.out.println(e.getReadableCause()));
//            solutionApproachAnalyser.setQfSemanticSettings(qfSemanticSetting);
//            solutionApproachAnalyser.check().forEach(e -> System.out.println(e.getErrorCode()));


    }


}
