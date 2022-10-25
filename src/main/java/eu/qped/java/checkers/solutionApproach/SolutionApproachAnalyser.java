package eu.qped.java.checkers.solutionApproach;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import eu.qped.java.checkers.solutionApproach.checkReport.SolutionApproachReportEntry;
import lombok.*;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    // delete
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean usedALoop;

    // delete
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String returnType;

    // delete
    private ArrayList<SemanticFeedback> feedbacks;


    public List<SolutionApproachReportEntry> check() {

        List<SolutionApproachReportEntry> solutionApproachEntries = new ArrayList<>();

        SemanticSettingReader reader = SemanticSettingReader.builder().qfSemanticSettings(qfSemanticSettings).build();
        var settings = reader.groupByFileName();

        // per File
        settings.forEach(
                fileSettingEntry -> {
                    if (targetProjectPath == null) {
                        targetProjectPath = "";
                    }
                    var path = "";
                    if (fileSettingEntry.getFilePath() != null && !"".equals(fileSettingEntry.getFilePath())) {
                        if ('/' == fileSettingEntry.getFilePath().charAt(0)) {
                            path = targetProjectPath + fileSettingEntry.getFilePath();
                        } else {
                            if (!Objects.equals(targetProjectPath, "")) {
                                path = targetProjectPath + "/" + fileSettingEntry.getFilePath();
                            } else {
                                path = fileSettingEntry.getFilePath();
                            }
                        }
                    } else path = targetProjectPath;  // answer is string

                    var compilationUnit = parse(path); // AST per File
                    // AST per Method in File
                    fileSettingEntry.getSettingItems().forEach(
                            semanticSettingItem -> {
                                try {
                                    // analytics phase
                                    SolutionApproachReportEntry basicReportEntry = SolutionApproachReportEntry.builder().relatedSemanticSettingItem(semanticSettingItem).build();
                                    BlockStmt targetedMethod = getTargetedMethod(compilationUnit, basicReportEntry); // get target method and the return type of the target method
                                    StatementsVisitorHelper statementsVisitorHelper = StatementsVisitorHelper.createStatementsVisitorHelper(targetedMethod);
                                    MethodCalledChecker recursiveCheckHelper = MethodCalledChecker.createRecursiveCheckHelper(targetedMethod);
                                    updateReportEntryFields(basicReportEntry, statementsVisitorHelper, recursiveCheckHelper); // update basic Report Entry
                                    // create report Entries from basic report Entry
                                    solutionApproachEntries.addAll(generateLoopReportEntries(basicReportEntry));

//                                    generateSemanticStatementsFeedback(semanticSettingItem, statementsVisitorHelper);
                                    generateSemanticRecursionFeedback(semanticSettingItem, recursiveCheckHelper);
                                    checkReturnTyp(semanticSettingItem.getReturnType());
                                } catch (NoSuchMethodException e) {
                                    System.out.println(e.getMessage() + " " + e.getCause());
                                }
                            }
                    );
                }
        );
        return solutionApproachEntries;
    }

    private List<SolutionApproachReportEntry> generateLoopReportEntries(SolutionApproachReportEntry basicReportEntry) {
        List<SolutionApproachReportEntry> result = new ArrayList<>();
        var semanticSettingItem = basicReportEntry.getRelatedSemanticSettingItem();
        if (semanticSettingItem.getWhileLoop() != -1 && basicReportEntry.getSolutionWhileCounter() > semanticSettingItem.getWhileLoop() ) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("MoreThenExpectedWhileLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getForLoop() != -1 && basicReportEntry.getSolutionForCounter() > semanticSettingItem.getForLoop() ) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("MoreThenExpectedForLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getForEachLoop() != -1 && basicReportEntry.getSolutionForEachCounter() > semanticSettingItem.getForEachLoop()   ) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("MoreThenExpectedForEachLoops");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getIfElseStmt() != -1 && basicReportEntry.getSolutionIfElseCounter() > semanticSettingItem.getIfElseStmt() ) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("MoreThenExpectedIfElseStatement");
            result.add(reportEntry);
        }
        if (semanticSettingItem.getDoWhileLoop() != -1 && basicReportEntry.getSolutionDoWhileCounter() > semanticSettingItem.getDoWhileLoop()  ) {
            var reportEntry = cloneBasicReportEntry(basicReportEntry);
            reportEntry.setErrorCode("MoreThenExpectedDoWhileLoops");
            result.add(reportEntry);
        }
        return result;
    }

    private SolutionApproachReportEntry cloneBasicReportEntry(SolutionApproachReportEntry basicReportEntry) {
        return SolutionApproachReportEntry.builder()
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

    private void updateReportEntryFields(SolutionApproachReportEntry solutionApproachReportEntry, StatementsVisitorHelper statementsVisitorHelper, MethodCalledChecker recursiveCheckHelper) {
        solutionApproachReportEntry.setSolutionHasRecursiveMethodCall(recursiveCheckHelper.check(solutionApproachReportEntry.getRelatedSemanticSettingItem().getMethodName()));
        solutionApproachReportEntry.setSolutionForCounter(statementsVisitorHelper.getForCounter());
        solutionApproachReportEntry.setSolutionForEachCounter(statementsVisitorHelper.getForEachCounter());
        solutionApproachReportEntry.setSolutionDoWhileCounter(statementsVisitorHelper.getDoCounter());
        solutionApproachReportEntry.setSolutionWhileCounter(statementsVisitorHelper.getWhileCounter());
        solutionApproachReportEntry.setSolutionIfElseCounter(statementsVisitorHelper.getIfElseCounter());
        solutionApproachReportEntry.setSolutionHasLoop(
                solutionApproachReportEntry.getSolutionWhileCounter() > 0
                        || solutionApproachReportEntry.getSolutionDoWhileCounter() > 0
                        || statementsVisitorHelper.getForCounter() > 0
                        || statementsVisitorHelper.getForEachCounter() > 0
        );

    }

    private CompilationUnit parse(final String path) {
        return parseFromResourceType(path);
    }

    private CompilationUnit parseFromResourceType(String path) {
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

    private BlockStmt getTargetedMethod(CompilationUnit compilationUnit, SolutionApproachReportEntry basicReportEntry) throws NoSuchMethodException {
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

    private void checkReturnTyp(String targetReturnType) {
        boolean result = false;
        result = targetReturnType.equalsIgnoreCase(returnType);

        if (!result && !targetReturnType.equals("undefined") && targetReturnType != null) {
            feedbacks.add(new SemanticFeedback("you've used the return type " + returnType + "\n" + " you should use the return type " + targetReturnType));
        }
    }

    private void generateSemanticStatementsFeedback(SemanticSettingItem settingItem, StatementsVisitorHelper statementsVisitorHelper) {

        if (statementsVisitorHelper.getWhileCounter() > settingItem.getWhileLoop() && settingItem.getWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getWhileLoop() + " while loop in your code, but you've used " + statementsVisitorHelper.getWhileCounter() + " while loop in " + settingItem.getFilePath()));
        }
        if (statementsVisitorHelper.getForCounter() > settingItem.getForLoop() && settingItem.getForLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getForLoop() + " for loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  for loop in " + settingItem.getFilePath()));
        }
        if (statementsVisitorHelper.getForEachCounter() > settingItem.getForEachLoop() && settingItem.getForEachLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getForEachLoop() + " forEach loop in your code, but you've used " + statementsVisitorHelper.getForEachCounter() + "  forEach loop in " + settingItem.getFilePath()));
        }
        if (statementsVisitorHelper.getIfElseCounter() > settingItem.getIfElseStmt() && settingItem.getIfElseStmt() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getIfElseStmt() + " IfElse Statement in your code, but you've used " + statementsVisitorHelper.getIfElseCounter() + "  ifElse Statment in " + settingItem.getFilePath()));
        }
        if (statementsVisitorHelper.getDoCounter() > settingItem.getDoWhileLoop() && settingItem.getDoWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getDoWhileLoop() + " doWhile loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  doWhile loop in " + settingItem.getFilePath()));
        }
    }

    private void generateSemanticRecursionFeedback(SemanticSettingItem settingItem, MethodCalledChecker recursiveCheckHelper) {
        var hasUsedRecursive = recursiveCheckHelper.check(settingItem.getMethodName());
        if ((!hasUsedRecursive && settingItem.getRecursive() && !usedALoop)) {
            feedbacks.add(new SemanticFeedback("you have to solve the method recursive in " + settingItem.getFilePath()));
        } else if (settingItem.getRecursive() && usedALoop && hasUsedRecursive) {
            feedbacks.add(new SemanticFeedback("you have used a Loop with your recursive Call in " + settingItem.getFilePath()));
        } else if (settingItem.getRecursive() && usedALoop && !hasUsedRecursive) {
            feedbacks.add(new SemanticFeedback("you have used a Loop without a recursive Call, you have to solve it just recursive in " + settingItem.getFilePath()));
        } else if (hasUsedRecursive && !settingItem.getRecursive()) {
            feedbacks.add(new SemanticFeedback("yor are not allowed to use recursive in the method in " + settingItem.getFilePath() + " " + settingItem.getMethodName()));
        }
    }


    public static void main(String[] args) throws IOException {

        List<SemanticSettingItem> settingItems = new ArrayList<>();

        System.out.println(Path.of("tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java"));

        var bagCalcPriceSettingItem = SemanticSettingItem.builder()
                .filePath("/tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java")
                .methodName("calcPrice")
                .returnType("void")
                .whileLoop(0)
                .build();
        var bagCalcRecSettingItem = SemanticSettingItem.builder()
                .filePath("/tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java")
                .methodName("calcRec")
                .returnType("int")
                .recursive(false)
                .build();

        settingItems.add(bagCalcPriceSettingItem);
        settingItems.add(bagCalcRecSettingItem);

        SolutionApproachAnalyser solutionApproachAnalyser = SolutionApproachAnalyser.builder().build();

        solutionApproachAnalyser.parse("tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java");

    }
}
