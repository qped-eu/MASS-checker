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
        checkReturnTyp();
        if(checkMethodExist()) {
            executeStmtVisitor();
            if (Boolean.parseBoolean(semanticConfigurator.getRecursionAllowed())){
                executeRecursionChecker();
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

    private BlockStmt getTargetedMethod (String targetedMethodName) throws NoSuchMethodException {
        final BlockStmt[] result = {new BlockStmt()};
//        final boolean[] methodFound = {false};
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
//                                methodFound[0] = true;
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
//        if (!methodFound[0]){
        if (!methodFound.getValue()){
            throw new NoSuchMethodException(NoSuchMethodException.class.getName() + " the Method: " + targetedMethodName + " not found!");
        }
        return result[0];
    }

    private void executeStmtVisitor()  {
        try {
            String methodName = semanticConfigurator.getMethodName();
            StatementsVisitorHelper statementsVisitorHelper = new StatementsVisitorHelper(getTargetedMethod(methodName));

            int targetedWhile = semanticConfigurator.getWhileLoop();
            int targetedFor = semanticConfigurator.getForLoop();
            int targetedForEach = Integer.parseInt(semanticConfigurator.getForEachLoop());
            int targetedIfElse = Integer.parseInt(semanticConfigurator.getIfElseStmt());
            int targetedDoWhile = Integer.parseInt(semanticConfigurator.getDoWhileLoop());


            usedALoop = statementsVisitorHelper.getWhileCounter() > 0
                    || statementsVisitorHelper.getDoCounter() >0
                    || statementsVisitorHelper.getForEachCounter() >0
                    || statementsVisitorHelper.getForCounter() > 0;

            if (statementsVisitorHelper.getWhileCounter() > targetedWhile && targetedWhile != -1){
                feedbacks.add(new SemanticFeedback("You should not use no more than "+targetedWhile+" while loop in your code, but you've used "+ statementsVisitorHelper.getWhileCounter()+" while loop "));
            }
            if (statementsVisitorHelper.getForCounter() > targetedFor && targetedFor != -1){
                feedbacks.add(new SemanticFeedback("You should not use no more than "+targetedFor+" for loop in your code, but you've used "+ statementsVisitorHelper.getForCounter()+"  for loop "));
            }
            if (statementsVisitorHelper.getForEachCounter() > targetedForEach && targetedForEach != -1){
                feedbacks.add(new SemanticFeedback("You should not use no more than "+targetedForEach+" forEach loop in your code, but you've used "+ statementsVisitorHelper.getForEachCounter()+"  forEach loop "));
            }
            if ( statementsVisitorHelper.getIfElseCounter()  > targetedIfElse && targetedIfElse != -1){
                feedbacks.add(new SemanticFeedback("You should not use no more than "+targetedIfElse+" IfElse Statement in your code, but you've used "+ statementsVisitorHelper.getIfElseCounter()+"  ifElse Statment "));
            }
            if (statementsVisitorHelper.getDoCounter() > targetedDoWhile && targetedDoWhile != -1){
                feedbacks.add(new SemanticFeedback("You should not use no more than "+targetedDoWhile+" doWhile loop in your code, but you've used "+ statementsVisitorHelper.getForCounter()+"  doWhile loop "));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage() + " " + e.getCause());
        }

    }

    private boolean checkMethodExist() {
        String methodName = semanticConfigurator.getMethodName();
        if (methodName == null || methodName.equals("undefined")){
            feedbacks.add(new SemanticFeedback("Method not found"));
            return false;
        }
        return true;
    }
    private void executeRecursionChecker()  {
        try {
            String methodName = semanticConfigurator.getMethodName();
            boolean mustRec = Boolean.parseBoolean(semanticConfigurator.getRecursionAllowed());
            RecursiveCheckHelper recursiveCheckHelper = new RecursiveCheckHelper(getTargetedMethod(methodName), methodName);
            if ((!recursiveCheckHelper.check() && mustRec && !usedALoop)) {
                feedbacks.add(new SemanticFeedback("you have to solve the method recursive"));
            } else if (mustRec && usedALoop && recursiveCheckHelper.check()) {
                feedbacks.add(new SemanticFeedback("you have used a Loop with your recursive Call"));
            } else if (mustRec && usedALoop && !recursiveCheckHelper.check()) {
                feedbacks.add(new SemanticFeedback("you have used a Loop without a recursive Call, you have to solve it just recursive"));
            } else {
                feedbacks.add(new SemanticFeedback("well done!"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    public static void main(String[] args) throws Exception {
//
//        long start = System.nanoTime();
//
//        Map<String , String> settings = new HashMap<>();
//        settings.put("methodName" , "a");
//        settings.put("recursionAllowed" , "true");
//        settings.put("whileLoop" , "1");
//        settings.put("forLoop" , "1");
//        settings.put("forEachLoop" , "1");
//        settings.put("ifElseStmt" , "2");
//        settings.put("doWhileLoop" , "0");
//        settings.put("returnType" , "void");
//
//        SemanticConfigurator semanticConfigurator = new SemanticConfigurator(settings);
//
//
//        String source =  "/*** Test class*/import java.util.*;class TestClass {" +
//                "    private int a(int a, boolean b , String c ,double e){\n" +
//                "int x;"+
//                "        return x;\n" +
//                "    }\n" +
//                "    private boolean b(List a ,ArrayList b ,float c){\n" +
//                "        return false;\n" +
//                "    }" +
//                "}";
//        SemanticChecker semanticChecker1 = createSemanticChecker(source , semanticConfigurator);
//        semanticChecker1.executeStmtVisitor();
//        semanticChecker1.executeRecursionChecker();
//        semanticChecker1.printFeedbacks();
//
////        SemanticChecker semanticChecker = new SemanticChecker(source);
//        //semanticChecker.getTargetedMethod("test");
////        semanticChecker.executeStmtVisitor();
//        //semanticChecker.executeRecursionChecker();
//
//        long end = System.nanoTime() -start;
//        System.out.println(end * Math.pow(10 , -9));
    }
}
