package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;
import net.sf.saxon.expr.Component;

import java.util.List;

public class Log extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() < 1 || list.size() > 2) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        float value;
        try{
            float x = (float) list.get(0).evaluate(this);
            float base = (float) list.get(1).evaluate(this);
            result = (float) (Math.log(x) / Math.log(base));
        }catch (IndexOutOfBoundsException ee) {
            try {
                value = (float) list.get(0).evaluate(this);
                result = (float) Math.log(value);
            } catch (ClassCastException e) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        } catch (ClassCastException eee){
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Log" + "(" + super.getId() + ")";
    }
}
