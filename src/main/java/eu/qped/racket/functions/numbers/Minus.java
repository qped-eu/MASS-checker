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
        int count = 0;
        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            for (Class<?> clazz : opNum.arrayList) {
                count++;
                System.out.println("Class 11: " + clazz.getName());
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                    System.out.println("Yes 1");
                    if (first) {
                        result = (float) e.evaluate(this);
                        first = false;
                        System.out.println("Yes 2");
                    } else
                        result -= (float) e.evaluate(this);
                } else {
                    if (opNum.arrayList.size() == count) {
                        System.out.println("dshdfsfhaldh" + opNum.arrayList.size());
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
