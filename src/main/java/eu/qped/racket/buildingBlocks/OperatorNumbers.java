package eu.qped.racket.buildingBlocks;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.functions.numbers.*;

import java.util.ArrayList;
import java.util.Arrays;

public class OperatorNumbers {

    public Plus plus = new Plus();
    public Minus minus = new Minus();
    public Division division = new Division();
    public Sub1 sub1 = new Sub1();
    public Sqrt sqrt = new Sqrt();
    public Sqr sqr = new Sqr();
    public Round round = new Round();
    public Random random = new Random();
    public Multiplication multiplication = new Multiplication();
    public Modulo modulo = new Modulo();
    public Min min = new Min();
    public Max max = new Max();
    public Log log = new Log();
    public Floor floor = new Floor();
    public Exp exp = new Exp();
    public Ceiling ceiling = new Ceiling();
    public Add1 add1 = new Add1();
    public Absolute absolute = new Absolute();
    //public ArrayList<Class> arrayList = new ArrayList<>(Arrays.asList(plus.getClass(), minus.getClass(), division.getClass()));
    public ArrayList<Class> arrayList = new ArrayList<>(Arrays.asList(minus.getClass(), plus.getClass(), division.getClass(), sub1.getClass(),
                                                                sqrt.getClass(), sqr.getClass(), round.getClass(), random.getClass(),
                                                                multiplication.getClass(), modulo.getClass(), min.getClass(), max.getClass(),
                                                                log.getClass(), floor.getClass(), exp.getClass(), ceiling.getClass(), add1.getClass(),
                                                                absolute.getClass()));

}
