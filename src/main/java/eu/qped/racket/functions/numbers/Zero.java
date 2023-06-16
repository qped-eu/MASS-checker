package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Zero extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        boolean returnBoolean = false;
        OperatorNumbers opNum = new OperatorNumbers();
        int count = 0;
        for (Class<?> clazz : opNum.arrayList) {
            count++;
            if(list.get(0) instanceof Number || clazz.isInstance(list.get(0).getParts().get(0))) {
                returnBoolean = ((float) (list.get(0).evaluate(this)) == 0);
                break;
            } else {
                if (opNum.arrayList.size() == count) {
                    throw new Exception("Expression isnt instance of Number");
                }
            }
        }
        return returnBoolean;
    }

    @Override
    public String toString() {
        return "Zero?" + "(" + super.getId() + ")";
    }
}
