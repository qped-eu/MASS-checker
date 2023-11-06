package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class EmptyQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        try {
            if ((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {      //String
//                boolean isEmpty = (list.get(0).getClass().equals(new Empty().getClass()));
//                if(!isEmpty){
//                    isEmpty = ((String)list.get(0).evaluate(this)).compareTo("'()") == 0;
//                }
                return ((String)list.get(0).evaluate(this)).compareTo("'()") == 0;
            } else {
                throw new Exception("Expression isnt instance of List");
            }
        } catch (IndexOutOfBoundsException e){      //Class Empty,
            if(list.get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0) instanceof Cons || list.get(0) instanceof Empty){
                return list.get(0).getClass().equals(new Empty().getClass());
            } else {
                throw new Exception("Expression isnt instance of List");
            }
        }
//        return Boolean.toString(list.get(0).getClass().equals(new Empty().getClass()));
    }

    @Override
    public String toString() {
        return "Empty? "  + " (" + super.getId() + ")";
    }
}
