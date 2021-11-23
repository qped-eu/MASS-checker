package eu.qped.umr.checkers.styleChecker.externalRules.design;

import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

/**
 * Rule to detect Do-While Statement
 * @author Basel Alaktaa
 * @since 15.07.2021
 * @version 1.0
 */

public class AllowDoWhileStmtRule extends AbstractJavaRule {

    private final static PropertyDescriptor<Boolean> DO_While_ALLOWED =
            PropertyFactory.booleanProperty("allowed")
                    .desc("Trigger the Violation if Do-While Stmt has been found")
                    .defaultValue(true)
                    .build();


    public AllowDoWhileStmtRule(){
        definePropertyDescriptor(DO_While_ALLOWED);
    }

    @Override
    public Object visit (ASTDoStatement node, Object data){

        boolean stmtAllowed = getProperty(DO_While_ALLOWED);
        if (!stmtAllowed){
            addViolation(data, node);
        }
        return super.visit(node , data);
    }
}
