package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.*;
import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Number;

import java.util.ArrayList;
import java.util.List;

public class MakeList extends Expression {
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

        String s = "";
        int counter = 0;
        OperatorNumbers opNum = new OperatorNumbers();
        ArrayList<Class> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(opNum.arrayList);
        arrayListAll.addAll(opNum.boolArrayList);
        try {
                float number = (float) list.get(0).evaluate(this);
                System.out.println(number);
                if(list.get(1) instanceof Boolean || list.get(1).getParts().size() > 0 && opNum.boolArrayList.contains(list.get(1).getParts().get(0).getClass())) {
                    while (number > 0) {
                        boolean valueB = (boolean) list.get(1).evaluate(this);
                        s += "(cons " + valueB + " ";
                        number--;
                        counter++;
                    }
                    s += "'()";
                    while (counter > 0) {
                        s += ")";
                        counter--;
                    }
                }
                if(list.get(1) instanceof Number || list.get(1).getParts().size() > 0 && opNum.arrayList.contains(list.get(1).getParts().get(0).getClass())) {
                    while (number > 0) {
                        float valueF = (float) list.get(1).evaluate(this);
                        s += "(cons " + valueF + " ";
                        number--;
                        counter++;
                    }
                    s += "'()";
                    while (counter > 0) {
                        s += ")";
                        counter--;
                    }
                }
                if(list.get(1) instanceof StringR) {
                    while (number > 0) {
                        String valueS = (String) list.get(1).evaluate(this);
                        s += "(cons " + valueS + " ";
                        number--;
                        counter++;
                    }
                    s += "'()";
                    while (counter > 0) {
                        s += ")";
                        counter--;
                    }
                }
            } catch (ClassCastException ee) {
            String stException = "Expression isnt instance of Boolean/expects a boolean";
            throw new Exception(stException);
        }
        return s;


//        String s = "";
//        float number = (float) list.get(0).evaluate(this);
//        String value = (String) list.get(1).evaluate(this);
//        int counter = 0;
//        while (number > 0) {
//            s += "(cons " + value + " ";
//            number--;
//            counter++;
//        }
//        s += "'()";
//        while (counter > 0) {
//            s += ")";
//            counter--;
//        }
//        return s;
    }

    @Override
    public String toString() {
        return "make-list "  + " (" + super.getId() + ")";
    }
}
