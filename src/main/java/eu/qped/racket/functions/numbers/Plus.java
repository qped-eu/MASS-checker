package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;

import java.util.ArrayList;
import java.util.List;

public class Plus extends Expression {


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
        for (Expression e : list) {

                for (Class<?> clazz : opNum.arrayList) {
                    count++;
                    System.out.println("Class: " + clazz.getName());
                    if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {
                        System.out.println("Yes");
                        result += (float) e.evaluate(this);
                        break;
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
        //return result;
    }

//    public GenericType evaluate(List<Expression> list) {
//        float result = 0;
//        for (Expression e : list) {
//            result += e.number
//        }
//        return Float.toString(result);
//        //return result;
//    }

    @Override
    public String toString() {
        return "Plus" + "(" + super.getId() + ")";
    }
}