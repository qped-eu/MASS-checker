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
        OperatorNumbers opNum = new OperatorNumbers();
        boolean firstBoolean = false;
        boolean first = true;
        for (Expression e : list) {
            int count = 0;
            for (Class<?> clazz : opNum.boolArrayList) {
                count++;
                if (e instanceof Boolean || (e.getParts().size() > 0 && clazz.isInstance(e.getParts().get(0)))) {
                    if(first){
                        firstBoolean = (boolean) e.evaluate(this);
                        first = false;
                        break;
                    }
                    if(firstBoolean == (boolean) e.evaluate(this)){
                        return true;
                    }
                }
                else {
                    if (opNum.boolArrayList.size() == count) {
                        throw new Exception("Expression isnt instance of Boolean");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "BooleanEQ" + "(" + super.getId() + ")";
    }
}
