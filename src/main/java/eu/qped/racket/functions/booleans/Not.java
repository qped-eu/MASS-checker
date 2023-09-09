package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Not extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        for (Expression e : list) {
            try {
                Object result = e.evaluate(this);
                if (!(boolean) result) {
                    System.out.println(result.getClass());
                    return true;
                } else {
                    return false;
                }
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of Boolean/expects a boolean";
                throw new Exception(stException);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Not" + "(" + super.getId() + ")";
    }
}
