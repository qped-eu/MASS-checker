package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Equal extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();
        float result = 0;
        int count = 0;
        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            for (Class<?> clazz : opNum.arrayList) {
                count++;
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                    float valueNow = (float) e.evaluate(this);
                    if (first) {
                        value = valueNow;
                        first = false;
                        continue;
                    }
                    float epsilon = 1 * (float)Math.pow(10, -10); //The accepted deviation vor Float Values here 1*10^-10
                    if (!(Math.abs(value - valueNow) < epsilon)) {
                        return false;
                    }
                    value = valueNow;
                    break;
                } else {
                    if (opNum.arrayList.size() == count) {
                        throw new Exception("Expression isnt instance of Number");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Equal" + "(" + super.getId() + ")";
    }

}
