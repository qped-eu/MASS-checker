package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class BooleanEQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        boolean firstBoolean = false;
        boolean first = true;
        for (Expression e : list) {
            try {
                if (first) {
                    firstBoolean = (boolean) e.evaluate(this);
                    first = false;
                    continue;
                }
                if (firstBoolean == (boolean) e.evaluate(this)) {
                    return true;
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
        return "BooleanEQ" + "(" + super.getId() + ")";
    }
}
