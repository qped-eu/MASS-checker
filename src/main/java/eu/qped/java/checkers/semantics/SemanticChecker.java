package eu.qped.java.checkers.semantics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import lombok.Data;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class SemanticChecker {

    private  String source;
    private final SemanticConfigurator semanticConfigurator;
    private CompilationUnit compilationUnit;
    private final ArrayList<SemanticFeedback> feedbacks;
    private final Map<String , String> settings;
    private boolean usedALoop = false;
    private String targetReturnType;
    private String returnType;



//    private SemanticChecker(String source, SemanticConfigurator semanticConfigurator){
//        this.source = source;
//        this.feedbacks = new ArrayList<>();
//        this.semanticConfigurator = semanticConfigurator;
//        settings = new HashMap<>();
//        check();
//    }


    private SemanticChecker(SemanticConfigurator semanticConfigurator){
        this.feedbacks = new ArrayList<>();
        this.semanticConfigurator = semanticConfigurator;
        settings = new HashMap<>();
    }

    public static SemanticChecker createSemanticMassChecker(SemanticConfigurator semanticConfigurator) {
        return new SemanticChecker(semanticConfigurator);
    }

    public void check(){
        parseCompUnit();
        if(checkMethodExist()) {
            try {
                BlockStmt targetedMethod = getTargetedMethod(semanticConfigurator.getMethodName());
                StatementsVisitorHelper statementsVisitorHelper = StatementsVisitorHelper.createStatementsVisitorHelper(targetedMethod);
                calculateUsedLoop(statementsVisitorHelper);
                generateSemanticStatementsFeedback(statementsVisitorHelper);
                if (semanticConfigurator.getRecursionAllowed()){
                    MethodCalledChecker recursiveCheckHelper = MethodCalledChecker.createRecursiveCheckHelper(targetedMethod);
                    generateSemanticRecursionFeedback(recursiveCheckHelper);
                }
                checkReturnTyp();
            } catch (NoSuchMethodException e){
                System.out.println(e.getMessage() + " " + e.getCause());
            }
        }
    }

    private void checkReturnTyp(){
        boolean result = false;
        targetReturnType = semanticConfigurator.getReturnType();

        result = targetReturnType.equalsIgnoreCase(returnType);

        if (!result &&  !targetReturnType.equals("undefined") && targetReturnType != null){
            feedbacks.add(new SemanticFeedback("you've used the return type " + returnType + "\n" + " you should use the return type " + targetReturnType)) ;
        }
    }

    private void parseCompUnit (){
        this.compilationUnit = StaticJavaParser.parse(this.source);
    }

    private BlockStmt getTargetedMethod (String targetedMethodName)  throws NoSuchMethodException{
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
                            if (n.getName().toString().equals(targetedMethodName)){
                                returnType = n.getType().toString();
                                methodFound.setTrue();
                                if (n.getBody().isPresent()){
                                    result[0] = n.getBody().get().asBlockStmt();
                                }
                            }

                        }
                    } , null);
                }
            }
        }
        if (!methodFound.getValue()){
            throw new NoSuchMethodException(NoSuchMethodException.class.getName() + " the Method: " + targetedMethodName + " not found!");
        }
        return result[0];
    }

    private void generateSemanticStatementsFeedback(StatementsVisitorHelper statementsVisitorHelper)  {
        if (statementsVisitorHelper.getWhileCounter() > semanticConfigurator.getWhileLoop() && semanticConfigurator.getWhileLoop() != -1){
            feedbacks.add(new SemanticFeedback("You should not use no more than "+semanticConfigurator.getWhileLoop()+" while loop in your code, but you've used "+ statementsVisitorHelper.getWhileCounter()+" while loop "));
        }
        if (statementsVisitorHelper.getForCounter() > semanticConfigurator.getForLoop() && semanticConfigurator.getForLoop() != -1){
            feedbacks.add(new SemanticFeedback("You should not use no more than "+semanticConfigurator.getForLoop()+" for loop in your code, but you've used "+ statementsVisitorHelper.getForCounter()+"  for loop "));
        }
        if (statementsVisitorHelper.getForEachCounter() > semanticConfigurator.getForEachLoop() && semanticConfigurator.getForEachLoop() != -1){
            feedbacks.add(new SemanticFeedback("You should not use no more than "+semanticConfigurator.getForEachLoop()+" forEach loop in your code, but you've used "+ statementsVisitorHelper.getForEachCounter()+"  forEach loop "));
        }
        if ( statementsVisitorHelper.getIfElseCounter()  > semanticConfigurator.getIfElseStmt() && semanticConfigurator.getIfElseStmt() != -1){
            feedbacks.add(new SemanticFeedback("You should not use no more than "+semanticConfigurator.getIfElseStmt()+" IfElse Statement in your code, but you've used "+ statementsVisitorHelper.getIfElseCounter()+"  ifElse Statment "));
        }
        if (statementsVisitorHelper.getDoCounter() > semanticConfigurator.getDoWhileLoop() && semanticConfigurator.getDoWhileLoop() != -1){
            feedbacks.add(new SemanticFeedback("You should not use no more than "+semanticConfigurator.getDoWhileLoop()+" doWhile loop in your code, but you've used "+ statementsVisitorHelper.getForCounter()+"  doWhile loop "));
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
        if (methodName == null || methodName.equals("undefined")){
            feedbacks.add(new SemanticFeedback("Method not found"));
            return false;
        }
        return true;
    }

    private void generateSemanticRecursionFeedback(MethodCalledChecker recursiveCheckHelper)  {
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

}
