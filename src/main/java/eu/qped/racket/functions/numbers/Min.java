package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Min extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();
        Boolean first = true;
        float result = 0;
        int count = 0;
        for (Expression e : list) {
            for (Class<?> clazz : opNum.arrayList) {
                count++;
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                    float currentValue = (float) e.evaluate(this);
                    if (first) {
                        result = currentValue;
                        first = false;
                    }
                    if (result > currentValue) {
                        result = currentValue;
                    }
                    break;
                } else {
                    if (opNum.arrayList.size() == count) {
                        throw new Exception("Expression isnt instance of Number");
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Min" + "(" + super.getId() + ")";
    }
}
