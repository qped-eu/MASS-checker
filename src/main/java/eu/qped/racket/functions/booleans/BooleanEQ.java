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

        if (list.size() != 2) {
            String stException = "expects only 2 argument, but found " + list.size();
            throw new Exception(stException);
        }

        for (Expression e : list) {
            try {
                if (first) {
                    firstBoolean = (boolean) e.evaluate(this);
                    first = false;
                    continue;
                }
                if (!(firstBoolean == (boolean) e.evaluate(this))) {
                    return false;
                }
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of Boolean/expects a boolean";
                throw new Exception(stException);
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "BooleanEQ" + "(" + super.getId() + ")";
    }
}
