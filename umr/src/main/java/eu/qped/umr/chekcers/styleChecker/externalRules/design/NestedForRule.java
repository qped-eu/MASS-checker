package eu.qped.umr.chekcers.styleChecker.externalRules.design;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

/**
 * Rule to detect For statements depth in code using Ast
 * @since 13.07.2021
 * @author Basel Alaktaa
 * @version 1.0
 */

public class NestedForRule extends AbstractJavaRule {

    private int depth;
    private int depthLimit;

    private static final PropertyDescriptor<Integer> STMT_DEPTH =
            PropertyFactory.intProperty("allowed_depth")
                    .desc("The for statement depth reporting threshold")
                    .require(positive())
                    .defaultValue(2)
                    .build();

    public NestedForRule(){
        definePropertyDescriptor(STMT_DEPTH);
    }

    @Override
    public Object visit (ASTCompilationUnit node, Object data){
        depth = 0;
        depthLimit = getProperty(STMT_DEPTH);
        return super.visit(node, data);
    }
    @Override
    public Object visit (ASTForStatement node, Object data) {
        depth++;
        super.visit(node, data);
        if (depth == depthLimit){
            addViolation(data , node);
        }
        depth--;
        return data;
    }
}
