package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.*;
import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Number;

import java.util.ArrayList;
import java.util.List;

public class Member extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 2) {
            String stException = "expects only 2 argument, but found " + list.size();
            throw new Exception(stException);
        }

        OperatorNumbers opNum = new OperatorNumbers();
        ArrayList<Class> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(opNum.arrayList);
        arrayListAll.addAll(opNum.boolArrayList);
        try {
                if(list.get(0) instanceof StringR) {      //String
                    String s = (String) list.get(0).evaluate(this);
                    return list.get(1).evaluate(this).toString().contains(s);
                }
                if(list.get(0) instanceof Boolean || list.get(0).getParts().size() > 0 && opNum.boolArrayList.contains(list.get(0).getParts().get(0).getClass())) {        //Boolean
                    boolean b = (boolean) list.get(0).evaluate(this);
                    return list.get(1).evaluate(this).toString().contains(String.valueOf(b));
                }
                if(list.get(0) instanceof Number || list.get(0).getParts().size() > 0 && opNum.arrayList.contains(list.get(0).getParts().get(0).getClass())) {         //Number
                    Float f = (Float) list.get(0).evaluate(this);
                    return list.get(1).evaluate(this).toString().contains(f.toString());
                }
            } catch (ClassCastException ee) {
            String stException = "Expression isnt instance of Boolean/expects a boolean";
            throw new Exception(stException);
        }
        return false;

        //////////////

//        if(list.get(0).evaluate(this) instanceof Number){
//
//        }
//
//
//
//        return list.get(1).evaluate(this).toString().contains((CharSequence) list.get(0).evaluate(this));
    }

    @Override
    public String toString() {
        return "Member "  + " (" + super.getId() + ")";
    }
}
