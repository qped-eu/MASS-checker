package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;

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
        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            for (Class<?> clazz : opNum.arrayList) {
                System.out.println("Class: " + clazz.getName());
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                    if (first) {
                        result = (float) e.evaluate(this);
                        first = false;
                        System.out.println("Yes");
                    } else
                        result -= (float) e.evaluate(this);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Minus" + "(" + super.getId() + ")";
    }

}
