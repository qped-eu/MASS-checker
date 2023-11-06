package eu.qped.racket.buildingBlocks;

import eu.qped.racket.functions.numbers.*;

import java.util.ArrayList;
import java.util.Arrays;

public class OperatorBoolean {

    public Zero zero = new Zero();
    public Positive positive = new Positive();
    public Odd odd = new Odd();
    public Negative negative = new Negative();
    public LessThan lessthan = new LessThan();
    public LessOrEqualThan lessorequalthan = new LessOrEqualThan();
    public GreaterThan greaterthan = new GreaterThan();
    public GreaterOrEqualThan greaterorequalthan = new GreaterOrEqualThan();
    public Even even = new Even();
    public Equal equal = new Equal();
    public ArrayList<Class> arrayList = new ArrayList<>(Arrays.asList(zero.getClass(), positive.getClass(), odd.getClass(), negative.getClass(),
                                                                lessthan.getClass(), lessorequalthan.getClass(), greaterthan.getClass(),
                                                                greaterorequalthan.getClass(), even.getClass(), equal.getClass()));
}
