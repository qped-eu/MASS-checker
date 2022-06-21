package eu.qped.java.checkers.semantics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.SourceRoot;
import eu.qped.java.checkers.mass.QFSemSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SemanticChecker {

    private QFSemSettings qfSemSettings;

    private String source;
    private SemanticConfigurator semanticConfigurator;
    private CompilationUnit compilationUnit;
    private ArrayList<SemanticFeedback> feedbacks;
    private Map<String, String> settings;
    private boolean usedALoop = false;
    private String targetReturnType;
    private String returnType;

    private SemanticChecker(SemanticConfigurator semanticConfigurator) {
        this.feedbacks = new ArrayList<>();
        this.semanticConfigurator = semanticConfigurator;
        settings = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.parse("exam-results/src/model/GrayCode.java");
    }

    public void parse(final String path) {
        File maybeFile = new File(path);
        if (maybeFile.isFile()) {
            parseFromResourceType(path, ResourceType.FILE);
        } else if (maybeFile.isDirectory()) {
            parseFromResourceType(path, ResourceType.DIR);
        } else throw new IllegalArgumentException(path + " is not a file or directory valid path!");
    }

    //FIXME written quick and dirty
    private void parseFromResourceType(final String path, ResourceType type) {
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser javaParser = new JavaParser(configuration);
        if (ResourceType.FILE == type) {
            try {
                var unit = javaParser.parse(Path.of(path));
                if (unit.getResult().isPresent()) {
                    this.compilationUnit = unit.getResult().get();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        } else if (ResourceType.DIR == type) {
            SourceRoot sourceRoot = new SourceRoot(Path.of(path));
            try {
                var parsedUnits = sourceRoot.tryToParse();
                //FIXME now we assume that we have just one File
                if (parsedUnits.size() > 0) {
                    var maybeUnit = parsedUnits.get(0);
                    if (maybeUnit.getResult().isPresent()) {
                        this.compilationUnit = maybeUnit.getResult().get();
                    }
                } else {
                    System.out.println("No Files");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void parseCompUnit() {
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser javaParser = new JavaParser(configuration);
        var unit = javaParser.parse(this.source);
        if (unit.getResult().isPresent()) {
            this.compilationUnit = unit.getResult().get();
        }
    }

    public void check() {
        parse(semanticConfigurator.getFilePath());
        if (checkMethodExist()) {
            try {
                BlockStmt targetedMethod = getTargetedMethod(semanticConfigurator.getMethodName());
                StatementsVisitorHelper statementsVisitorHelper = StatementsVisitorHelper.createStatementsVisitorHelper(targetedMethod);
                calculateUsedLoop(statementsVisitorHelper);
                generateSemanticStatementsFeedback(statementsVisitorHelper);
                if (semanticConfigurator.getRecursionAllowed()) {
                    MethodCalledChecker recursiveCheckHelper = MethodCalledChecker.createRecursiveCheckHelper(targetedMethod);
                    generateSemanticRecursionFeedback(recursiveCheckHelper);
                }
                checkReturnTyp();
            } catch (NoSuchMethodException e) {
                System.out.println(e.getMessage() + " " + e.getCause());
            }
        }
    }

    public static SemanticChecker createSemanticMassChecker(SemanticConfigurator semanticConfigurator) {
        return new SemanticChecker(semanticConfigurator);
    }

    private void checkReturnTyp() {
        boolean result = false;
        targetReturnType = semanticConfigurator.getReturnType();

        result = targetReturnType.equalsIgnoreCase(returnType);

        if (!result && !targetReturnType.equals("undefined") && targetReturnType != null) {
            feedbacks.add(new SemanticFeedback("you've used the return type " + returnType + "\n" + " you should use the return type " + targetReturnType));
        }
    }

    private BlockStmt getTargetedMethod(String targetedMethodName) throws NoSuchMethodException {
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

    private void generateSemanticStatementsFeedback(StatementsVisitorHelper statementsVisitorHelper) {
        if (statementsVisitorHelper.getWhileCounter() > semanticConfigurator.getWhileLoop() && semanticConfigurator.getWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + semanticConfigurator.getWhileLoop() + " while loop in your code, but you've used " + statementsVisitorHelper.getWhileCounter() + " while loop "));
        }
        if (statementsVisitorHelper.getForCounter() > semanticConfigurator.getForLoop() && semanticConfigurator.getForLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + semanticConfigurator.getForLoop() + " for loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  for loop "));
        }
        if (statementsVisitorHelper.getForEachCounter() > semanticConfigurator.getForEachLoop() && semanticConfigurator.getForEachLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + semanticConfigurator.getForEachLoop() + " forEach loop in your code, but you've used " + statementsVisitorHelper.getForEachCounter() + "  forEach loop "));
        }
        if (statementsVisitorHelper.getIfElseCounter() > semanticConfigurator.getIfElseStmt() && semanticConfigurator.getIfElseStmt() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + semanticConfigurator.getIfElseStmt() + " IfElse Statement in your code, but you've used " + statementsVisitorHelper.getIfElseCounter() + "  ifElse Statment "));
        }
        if (statementsVisitorHelper.getDoCounter() > semanticConfigurator.getDoWhileLoop() && semanticConfigurator.getDoWhileLoop() != -1) {
            feedbacks.add(new SemanticFeedback("You should not use no more than " + semanticConfigurator.getDoWhileLoop() + " doWhile loop in your code, but you've used " + statementsVisitorHelper.getForCounter() + "  doWhile loop "));
        }
    }

    private void calculateUsedLoop(StatementsVisitorHelper statementsVisitorHelper) {
        usedALoop = statementsVisitorHelper.getWhileCounter() > 0
                || statementsVisitorHelper.getDoCounter() > 0
                || statementsVisitorHelper.getForEachCounter() > 0
                || statementsVisitorHelper.getForCounter() > 0;
    }

    private boolean checkMethodExist() {
        String methodName = semanticConfigurator.getMethodName();
        if (methodName == null || methodName.equals("undefined")) {
            feedbacks.add(new SemanticFeedback("Method not found"));
            return false;
        }
        return true;
    }

    private void generateSemanticRecursionFeedback(MethodCalledChecker recursiveCheckHelper) {
        if ((!recursiveCheckHelper.check(semanticConfigurator.getMethodName()) && semanticConfigurator.getRecursionAllowed() && !usedALoop)) {
            feedbacks.add(new SemanticFeedback("you have to solve the method recursive"));
        } else if (semanticConfigurator.getRecursionAllowed() && usedALoop && recursiveCheckHelper.check(semanticConfigurator.getMethodName())) {
            feedbacks.add(new SemanticFeedback("you have used a Loop with your recursive Call"));
        } else if (semanticConfigurator.getRecursionAllowed() && usedALoop && !recursiveCheckHelper.check(semanticConfigurator.getMethodName())) {
            feedbacks.add(new SemanticFeedback("you have used a Loop without a recursive Call, you have to solve it just recursive"));
        } else {
            feedbacks.add(new SemanticFeedback("well done!"));
        }
    }

    private enum ResourceType {
        FILE,
        DIR,
        STRING
    }

}
