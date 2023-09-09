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
        if ((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons) && (list.get(1).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(1).getParts().get(0) instanceof Cons)) {      //String
            String st1 = (String) list.get(0).evaluate(this);
            String st2 = (String) list.get(1).evaluate(this);
            return st1.replace("'()", st2);
        } else {
            throw new Exception("Expression isnt instance of List");
        }

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
