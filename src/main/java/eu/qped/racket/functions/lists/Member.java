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

        OperatorNumbers opNum = new OperatorNumbers();
        ArrayList<Class> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(opNum.arrayList);
        arrayListAll.addAll(opNum.boolArrayList);
        int count = 0;
        for (Class<?> clazz : arrayListAll) {
            count++;
            if (list.get(0) instanceof Parameter || list.get(0) instanceof Boolean || list.get(0) instanceof Number || clazz.isInstance(list.get(0).getParts().get(0))) {
                if(list.get(0) instanceof Parameter) {      //String
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
            } else {
                if (arrayListAll.size() == count) {
                    throw new Exception("Expression isnt instance of Number");
                }
            }
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
