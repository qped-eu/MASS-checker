package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

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
            String st2 = st1.split("cons ")[2].split("\\s")[0];
            try{
                return Float.valueOf(st2);
            }catch (NumberFormatException ignored){
            }
            if(st2.equalsIgnoreCase("true") || st2.equalsIgnoreCase("#true") || st2.equalsIgnoreCase("#t")){
                return true;
            }
            if(st2.equalsIgnoreCase("false") || st2.equalsIgnoreCase("#false") || st2.equalsIgnoreCase("#f")){
                return false;
            }
            try{
                return ((Parameter) list.get(0).getParts().get(2).getParts().get(1)).getParaName();
            } catch (IndexOutOfBoundsException e){
            }
            return st2;
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
