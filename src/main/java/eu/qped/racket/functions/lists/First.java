package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.*;
import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Number;

import java.util.List;

public class First extends Expression {
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

        if((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {
            String st1 = (String) list.get(0).evaluate(this);

//            if(st1.equalsIgnoreCase("#false") || st1.equalsIgnoreCase("#f") || st1.equalsIgnoreCase("false")){      //Boolean
//                return false;
//            }
//            if(st1.equalsIgnoreCase("#true") || st1.equalsIgnoreCase("#t") || st1.equalsIgnoreCase("true")){        //Boolean
//                return true;
//            }
            if(list.get(0).getParts().get(1) instanceof Boolean){        //Boolean
                return (boolean) list.get(0).getParts().get(1).evaluate(this);
            }
            if(list.get(0).getParts().get(1) instanceof Number){        //Number
                return (float) list.get(0).getParts().get(1).evaluate(this);
            }
            if(list.get(0).getParts().get(1) instanceof StringR){        //String
                return (String) list.get(0).getParts().get(1).evaluate(this);
            }
            return st1.substring(5).split("\\s")[1];            //String
        } else {
            throw new Exception("Expression isnt instance of List");
        }

//        return list.get(0).evaluate(this).toString().substring(5).split("\\s")[1];
    }

    @Override
    public String toString() {
        return "first "  + " (" + super.getId() + ")";
    }
}
