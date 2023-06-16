package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Minus extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();
        int count = 0;
        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            for (Class<?> clazz : opNum.arrayList) {
                count++;
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                    if (first) {
                        result = (float) e.evaluate(this);
                        first = false;
                        break;
                    } else {
                        result -= (float) e.evaluate(this);
                        break;
                    }
                } else {
                    if (opNum.arrayList.size() == count) {
                        throw new Exception("Expression isnt instance of Number");
                    }
                }
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Minus" + "(" + super.getId() + ")";
    }

}
