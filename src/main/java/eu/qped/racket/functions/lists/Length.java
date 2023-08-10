package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Length extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        try {
            if ((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {      //String
                String st1 = (String) list.get(0).evaluate(this);

                return (float) st1.split("cons").length - 1;
            } else {
                throw new Exception("Expression isnt instance of List");
            }

//        return Float.toString(list.get(0).evaluate(this).toString().split("cons").length - 1);
        } catch(IndexOutOfBoundsException e){
            if ((list.get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0) instanceof Cons) || list.get(0) instanceof Empty) {      //String
                String st1 = (String) list.get(0).evaluate(this);

                return (float) st1.split("cons").length - 1;
            } else {
                throw new Exception("Expression isnt instance of List");
            }
        }
    }

    @Override
    public String toString() {
        return "Length "  + " (" + super.getId() + ")";
    }
}
