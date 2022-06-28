package eu.qped.java.checkers.semantics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import eu.qped.java.checkers.mass.QFSemSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import lombok.*;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SemanticChecker {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private QFSemSettings qfSemSettings;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private SemanticSettingReader semanticSettingReader;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean usedALoop;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String returnType;

    private ArrayList<SemanticFeedback> feedbacks;

    private String targetProjectPath;

    @Deprecated(forRemoval = true)
    public SemanticChecker(SemanticSettingReader semanticSettingReader) {

    }

    @Deprecated(forRemoval = true)
    public static SemanticChecker createSemanticMassChecker(SemanticSettingReader semanticSettingReader) {
        return new SemanticChecker(semanticSettingReader);
    }

    public void check() {
        SemanticSettingReader reader = SemanticSettingReader.builder().qfSemSettings(qfSemSettings).build();
        var settings = reader.groupByFileName();

        // per File
        settings.forEach(
                fileSettingEntry -> {
                    if (targetProjectPath == null) {
                        targetProjectPath = "";
                    }
                    var path = "";
                    if (fileSettingEntry.getFilePath().charAt(0) == '/') {
                        path = targetProjectPath + fileSettingEntry.getFilePath();
                    } else {
                        if (!Objects.equals(targetProjectPath, "")) {
                            path = targetProjectPath + "/" + fileSettingEntry.getFilePath();
                        } else {
                            path = fileSettingEntry.getFilePath();
                        }
                    }
                    if (path.charAt(0) == '/') {
                        path = path.substring(1);
                    }

                    var compilationUnit = parse(path); // AST per File

                    System.out.println("mero");

                    System.out.println(compilationUnit.toString());
                    System.out.println("mero");
                    // AST per Method in File
                    fileSettingEntry.getSettingItems().forEach(
                            semanticSettingItem -> {
                                try {
                                    BlockStmt targetedMethod = getTargetedMethod(compilationUnit, semanticSettingItem.getMethodName()); // This method throws NoSuchMethodException
                                    StatementsVisitorHelper statementsVisitorHelper = StatementsVisitorHelper.createStatementsVisitorHelper(targetedMethod);
                                    calculateUsedLoop(statementsVisitorHelper);
                                    generateSemanticStatementsFeedback(semanticSettingItem, statementsVisitorHelper);
                                    MethodCalledChecker recursiveCheckHelper = MethodCalledChecker.createRecursiveCheckHelper(targetedMethod);
                                    generateSemanticRecursionFeedback(semanticSettingItem, recursiveCheckHelper);
                                    checkReturnTyp(semanticSettingItem.getReturnType());
                                } catch (NoSuchMethodException e) {
                                    System.out.println(e.getMessage() + " " + e.getCause());
                                }
                            }
                    );
                }
        );
    }


    // src/main/java/eu/qped/java/checkers/style from syntax checker split([/])
    // eu.qped.um.java.checker.src..... split([.])
    private CompilationUnit parse(final String path) {
        System.out.println(path);
        return parseFromResourceType(path);
    }

    private CompilationUnit parseFromResourceType(final String path) {
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser javaParser = new JavaParser(configuration);
        try {
            System.out.println("------------------------");
            System.out.println("path:" + path);
            System.out.println("------------------------");
            var unit = javaParser.parse(Path.of(path));
            System.out.println("parseFromResourceType var unit");
            if (unit.getResult().isPresent()) {
                System.out.println("parseFromResourceType var unit is present");
                return unit.getResult().get();
            } else {
                System.out.println("parseFromResourceType var unit isnt present");
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            System.out.println("cause: " + e.getCause().toString());
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws IOException {

        List<SemanticSettingItem> settingItems = new ArrayList<>();

        var bagCalcPriceSettingItem = SemanticSettingItem.builder()
                .filePath("tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java")
                .methodName("calcPrice")
                .returnType("void")
                .whileLoop(0)
                .build();
        var bagCalcRecSettingItem = SemanticSettingItem.builder()
                .filePath("tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java")
                .methodName("calcRec")
                .returnType("int")
                .recursionAllowed(false)
                .build();

        settingItems.add(bagCalcPriceSettingItem);
        settingItems.add(bagCalcRecSettingItem);

        SemanticChecker semanticChecker = SemanticChecker.builder().build();

        semanticChecker.parseFromResourceType("tmp/exam-results62b874f9fb9d582f0b08d371/test-project/test-project/src/model/Bag.java");


//        QFSemSettings qfSemSettings = QFSemSettings.builder()
//                .semantic(settingItems)
//                .build();
//
//
//        SemanticChecker semanticChecker = SemanticChecker.builder().feedbacks(new ArrayList<>()).qfSemSettings(qfSemSettings).build();
//
//        semanticChecker.check();
//
//        semanticChecker.getFeedbacks().forEach(
//                System.out::println
//        );

    }

    //     private BlockStmt getTargetedMethod(compilationUnit ,String targetedMethodName) throws NoSuchMethodException {

    //     private BlockStmt getTargetedMethod(compilationUnit ,String targetedMethodName) throws NoSuchMethodException {
    private void checkReturnTyp(String targetReturnType) {
        boolean result = false;
        result = targetReturnType.equalsIgnoreCase(returnType);

        if (!result && !targetReturnType.equals("undefined") && targetReturnType != null) {
            feedbacks.add(new SemanticFeedback("you've used the return type " + returnType + "\n" + " you should use the return type " + targetReturnType));
        }
    }

    private BlockStmt getTargetedMethod(CompilationUnit compilationUnit, String targetedMethodName) throws NoSuchMethodException {
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
                            if (n.getName().toString().equals(targetedMethodName)) {
                                returnType = n.getType().toString();
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
            throw new NoSuchMethodException(NoSuchMethodException.class.getName() + " the Method: " + targetedMethodName + " not found!");
        }
        return result[0];
    }

    //     private BlockStmt getTargetedMethod(compilationUnit ,String targetedMethodName) throws NoSuchMethodException {
    private void generateSemanticStatementsFeedback(SemanticSettingItem settingItem, StatementsVisitorHelper statementsVisitorHelper) {
        if (statementsVisitorHelper.getWhileCounter() > settingItem.getWhileLoop() && settingItem.getWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getWhileLoop() + " while loop in your code, but you've used " + statementsVisitorHelper.getWhileCounter() + " while loop "));
        }
        if (statementsVisitorHelper.getForCounter() > settingItem.getForLoop() && settingItem.getForLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getForLoop() + " for loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  for loop "));
        }
        if (statementsVisitorHelper.getForEachCounter() > settingItem.getForEachLoop() && settingItem.getForEachLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getForEachLoop() + " forEach loop in your code, but you've used " + statementsVisitorHelper.getForEachCounter() + "  forEach loop "));
        }
        if (statementsVisitorHelper.getIfElseCounter() > settingItem.getIfElseStmt() && settingItem.getIfElseStmt() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getIfElseStmt() + " IfElse Statement in your code, but you've used " + statementsVisitorHelper.getIfElseCounter() + "  ifElse Statment "));
        }
        if (statementsVisitorHelper.getDoCounter() > settingItem.getDoWhileLoop() && settingItem.getDoWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + settingItem.getDoWhileLoop() + " doWhile loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  doWhile loop "));
        }
    }

    //     private BlockStmt getTargetedMethod(compilationUnit ,String targetedMethodName) throws NoSuchMethodException {
    private void calculateUsedLoop(StatementsVisitorHelper statementsVisitorHelper) {
        usedALoop = statementsVisitorHelper.getWhileCounter() > 0
                || statementsVisitorHelper.getDoCounter() > 0
                || statementsVisitorHelper.getForEachCounter() > 0
                || statementsVisitorHelper.getForCounter() > 0;
    }

    //     private BlockStmt getTargetedMethod(compilationUnit ,String targetedMethodName) throws NoSuchMethodException {
    private void generateSemanticRecursionFeedback(SemanticSettingItem settingItem, MethodCalledChecker recursiveCheckHelper) {
        var hasUsedRecursive = recursiveCheckHelper.check(settingItem.getMethodName());
        if ((!hasUsedRecursive && settingItem.getRecursionAllowed() && !usedALoop)) {
            feedbacks.add(new SemanticFeedback("you have to solve the method recursive"));
        } else if (settingItem.getRecursionAllowed() && usedALoop && hasUsedRecursive) {
            feedbacks.add(new SemanticFeedback("you have used a Loop with your recursive Call"));
        } else if (settingItem.getRecursionAllowed() && usedALoop && !hasUsedRecursive) {
            feedbacks.add(new SemanticFeedback("you have used a Loop without a recursive Call, you have to solve it just recursive"));
        } else if (hasUsedRecursive && !settingItem.getRecursionAllowed()) {
            feedbacks.add(new SemanticFeedback("yor are not allowed to use recursive in the method " + settingItem.getMethodName()));
        }
    }



}
