package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.*;
import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveAll extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 2) {
            String stException = "expects only 2 arguments, but found " + list.size();
            throw new Exception(stException);
        }

        OperatorNumbers opNum = new OperatorNumbers();
        ArrayList<Class> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(opNum.arrayList);
        arrayListAll.addAll(opNum.boolArrayList);
        int count = 0;
        String s = "";

        for (Class<?> clazz : arrayListAll) {
            count++;

            if (list.get(0) instanceof StringR || list.get(0) instanceof Boolean || list.get(0) instanceof eu.qped.racket.buildingBlocks.Number || clazz.isInstance(list.get(0).getParts().get(0))) {
                if(list.get(0) instanceof StringR) {      //String
                    String toRemove = (String) list.get(0).evaluate(this);
                    String inputList = (String) list.get(1).evaluate(this);

                    if (!inputList.contains(toRemove)) {
                        return inputList;
                    }

                    int countList = (int) Arrays.stream(inputList.split(toRemove)).count();
                    System.out.println(count);
                    s = inputList.replace("(cons " + toRemove + " ", "");
                    s = s.substring(0, s.length() - (countList - 1));
                    return s;
                }
                if(list.get(0) instanceof Boolean || list.get(0).getParts().size() > 0 && opNum.boolArrayList.contains(list.get(0).getParts().get(0).getClass())) {        //Boolean
                    boolean toRemoveBoolean = (boolean) list.get(0).evaluate(this);
                    String toRemove = String.valueOf(toRemoveBoolean);
                    String inputList = (String) list.get(1).evaluate(this);

                    if (!inputList.contains(toRemove)) {
                        return inputList;
                    }

                    int countList = (int) Arrays.stream(inputList.split(toRemove)).count();
                    System.out.println(count);
                    s = inputList.replace("(cons " + toRemove + " ", "");
                    s = s.substring(0, s.length() - (countList - 1));
                    return s;
                }
                if(list.get(0) instanceof Number || list.get(0).getParts().size() > 0 && opNum.arrayList.contains(list.get(0).getParts().get(0).getClass())) {         //Number
                    float toRemoveFloat = (float) list.get(0).evaluate(this);
                    String toRemove = String.valueOf(toRemoveFloat);
                    String inputList = (String) list.get(1).evaluate(this);

                    if (!inputList.contains(toRemove)) {
                        return inputList;
                    }

                    int countList = (int) Arrays.stream(inputList.split(toRemove)).count();
                    System.out.println(count);
                    s = inputList.replace("(cons " + toRemove + " ", "");
                    s = s.substring(0, s.length() - (countList - 1));
                    return s;
                }
            } else {
                if (arrayListAll.size() == count) {
                    try{
                        if(list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List) {      //String
                            String toRemove = (String) list.get(0).evaluate(this);
                            String inputList = (String) list.get(1).evaluate(this);

                            if (!inputList.contains(toRemove)) {
                                return inputList;
                            }

                            int countList = (int) Arrays.stream(inputList.split(toRemove)).count();
                            System.out.println(count);
                            s = inputList.replace("(cons " + toRemove + " ", "");
                            s = s.substring(0, s.length() - (countList - 1));
                            return s;
                        }
                    }catch(IndexOutOfBoundsException e){
                        throw new Exception("Expression isnt instance of Number");
                    }
                }
            }
        }
        return s;


//        String s = "";
//        String toRemove = (String) list.get(0).evaluate(this);
//        String inputList = (String) list.get(1).evaluate(this);
//
//        if (!inputList.contains(toRemove)) {
//            return inputList;
//        }
//        int countList = (int) Arrays.stream(inputList.split(toRemove)).count();
//        System.out.println(count);
//        s = inputList.replace("(cons " + toRemove + " ", "");
//        s = s.substring(0, s.length() - (countList - 1));
//        return s;
    }

    @Override
    public String toString() {
        return "Remove-All "  + " (" + super.getId() + ")";
    }
}
