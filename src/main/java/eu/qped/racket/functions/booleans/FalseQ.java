package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class FalseQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }


    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

            try {
                Object result = list.get(0).evaluate(this);
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

    @Override
    public String toString() {
        return "FalseQ" + "(" + super.getId() + ")";
    }
}
