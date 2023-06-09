package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;

import java.util.List;

public class Plus extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        for (Expression e : list) {
            if(e instanceof Number || e.getParts().get(0) instanceof Plus) {        //workaround für tiefere schachtelung e.getParts().get(0) instanceof
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                //System.out.println(e.evaluate(new Expression()));
                result += (float) e.evaluate(this);
                //result += (e.evaluate(this));
            } else {
                throw new Exception("Expression isnt instance of Number");
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