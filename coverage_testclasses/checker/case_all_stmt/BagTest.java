package adt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    public void constructorCalled() {
        new Bag();
    }

    @Test
    public void addCalled() {
        Bag toTest = new Bag();
        toTest.add(0);
        toTest.add(1);
    }


    @Test
    public void removeCalled() {
        Bag toTest = new Bag();
        toTest.remove(0);
        toTest.add(0);
        toTest.remove(0);
        toTest.add(1);
        toTest.remove(0);
    }

    @Test
    public void lengthCalled() {
        new Bag().length();
    }

    @Test
    public void getElemsCalled() {
        Bag toTest = new Bag();
        toTest.add(1);
        toTest.getElems();
    }

    @Test
    public void cardinalityCalled() {
        Bag toTest = new Bag();
        toTest.cardinality(0);
        toTest.add(1);
        toTest.cardinality(0);
        toTest.cardinality(1);
    }

}