package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.List;

public class Append extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String returnString = "'()";
        String st1;
        System.out.println(list.size());
        for (Expression e : list) {
            if ((e.getParts().get(0) instanceof eu.qped.racket.functions.lists.List || e.getParts().get(0) instanceof Cons)) {
                st1 = (String) e.evaluate(this);
                returnString = returnString.replace("'()", st1);
            } else {
                throw new Exception("Expression isnt instance of List");
            }
        }
        return returnString;

//        Expression c1 = list.get(0);
//        Expression c2 = list.get(1);
//
//        return c1.evaluate(this).toString().replace("'()",(String) c2.evaluate(this));
    }

    @Override
    public String toString() {
        return "Append " + " (" + super.getId() + ")";
    }
}
