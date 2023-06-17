package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class BooleanQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    //@Override
    //public Object evaluate(List<Expression> list) throws Exception {
    //    for (Expression e : list) {
    //        if (e.evaluate(this).equals("true") || e.evaluate(this).equals("false"))
    //            return Boolean.toString(true);
    //    }
    //    return Boolean.toString(false);
    //}

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();
        for (Expression e : list) {
            int count = 0;
            for (Class<?> clazz : opNum.boolArrayList) {
                count++;
                if (e instanceof Boolean || e.getParts().size() > 0 && clazz.isInstance(e.getParts().get(0))) {
                    if ((boolean) e.evaluate(this) || !(boolean) e.evaluate(this))        // (boolean)e.evaluate(this) == true || (boolean) e.evaluate(this) == false
                        return true;
                } else {
                    if (opNum.boolArrayList.size() == count) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "BooleanQ" + "(" + super.getId() + ")";
    }
}
