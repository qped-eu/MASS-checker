package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.ArrayList;
import java.util.List;

public class MakeList extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {

        String s = "";
        int counter = 0;

        OperatorNumbers opNum = new OperatorNumbers();
        ArrayList<Class> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(opNum.arrayList);
        arrayListAll.addAll(opNum.boolArrayList);
        int count = 0;
        for (Class<?> clazz : arrayListAll) {
            count++;
            if ((list.get(0) instanceof Number || clazz.isInstance(list.get(0).getParts().get(0))) && (list.get(1) instanceof Parameter || list.get(1) instanceof Boolean || list.get(1) instanceof Number || clazz.isInstance(list.get(1).getParts().get(0)))) {
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
                    break;
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
                    break;
                }
                if(list.get(1) instanceof Parameter) {
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
                    break;
                }
            } else {
                if (arrayListAll.size() == count) {
                    throw new Exception("Expression isnt instance of Number/Parameter/Boolean");
                }
            }
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
