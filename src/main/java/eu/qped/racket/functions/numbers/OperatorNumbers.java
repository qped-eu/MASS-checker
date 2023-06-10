package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.ArrayList;
import java.util.Arrays;

public class OperatorNumbers {

    public Plus plus = new Plus();
    public Minus minus = new Minus();
    public Division division = new Division();
    //public ArrayList<Class> arrayList = new ArrayList<>(Arrays.asList(plus.getClass(), minus.getClass(), division.getClass()));
    ArrayList<Class> arrayList = new ArrayList<>(Arrays.asList(plus.getClass(), minus.getClass(), division.getClass()));

}
