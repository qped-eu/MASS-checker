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
        new Bag().add(0);
    }


    @Test
    public void removeCalled() {
        new Bag().remove(0);
    }

    @Test
    public void lengthCalled() {
        new Bag().length();
    }

    @Test
    public void getElemsCalled() {
        new Bag().getElems();
    }

    @Test
    public void cardinalityCalled() {
        new Bag().cardinality(0);
    }

}