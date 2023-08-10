package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Second extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {      //String
            String st1 = (String) list.get(0).evaluate(this);
            return st1.split("cons ")[2].split("\\s")[0];
        } else {
            throw new Exception("Expression isnt instance of List");
        }
//        return list.get(0).evaluate(this).toString().split("cons ")[2].split("\\s")[0];
    }

    @Override
    public String toString() {
        return "second "  + " (" + super.getId() + ")";
    }
}
