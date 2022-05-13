package adt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BagTest {

    // Method cardinality is tested indirectly in all test methods.
    @Test
    void testCreat() {
        Bag bag = new Bag();
        assertEquals(0, bag.length(), "A new bag in instantiated, with length = 0");
    }

    @Test
    void testAddToEmptySet() {
        Bag bag = new Bag();
        boolean res = bag.add(1);
        assertEquals(1, bag.cardinality(1));
        assertEquals(1, bag.length(), "The new length is the old length plus 1");
        assertEquals(true, res, "returns true");
    }

    @Test
    void testAddToNonEmptyBag() {
        Bag bag = new Bag();
        bag.add(1);
        bag.add(2);
        boolean res = bag.add(3);
        assertEquals(1, bag.cardinality(3));
        assertEquals(3, bag.length(), "The new length is the old length plus 1");
        assertEquals(true, res, "returns true");
        res = bag.add(3);
        assertEquals(2, bag.cardinality(3));
        assertEquals(4, bag.length(), "The new length is the old length");
        assertEquals(true, res, "returns true");
    }

    @Test
    void testRemoveHappyPath() {
        Bag bag = new Bag();
        bag.add(1);
        bag.add(2);
        bag.add(3);
        boolean res = bag.remove(1);
        assertEquals(0, bag.cardinality(1));
        assertEquals(2, bag.length(), "The new length is the old length minus 1");
        assertEquals(true, res, "returns true");
    }

    @Test
    void testRemoveNonHappyPath() {
        // Length = 0
        Bag bag = new Bag();
        boolean res = bag.remove(1);
        assertEquals(0, bag.length(), "The new length is the old (= 0)");
        assertEquals(false, res, "returns false");

        // Bag does not contain el
        bag.add(1);
        bag.add(2);
        bag.add(3);
        res = bag.remove(4);
        assertEquals(3, bag.length(), "The new length is the old length (= 3)");
        assertEquals(false, res, "returns false");
    }

    @Test
    void testEquals() {
        Bag bag1 = new Bag();
        Bag bag2 = new Bag();
        bag2.add(1);
        bag2.add(2);
        Bag bag3 = new Bag();
        bag3.add(2);
        bag3.add(1);
        Bag bag4 = new Bag();
        bag4.add(1);
        bag4.add(2);
        bag4.add(3);
        assertFalse(bag1.equals(bag2), "The bags are not equal");
        assertTrue(bag2.equals(bag3), "The bags are equal");
        assertTrue(bag3.equals(bag2), "The bags are equal, symmetry");
        assertFalse(bag2.equals(bag4), "The bags are not equal");
    }

    @Test
    void testGetElems() {
        Bag bag = new Bag();
        int[] arr1 = new int[0];
        assertArrayEquals(arr1, bag.getElems());
        bag.add(0);
        bag.add(1);
        int[] arr2 = new int[2];
        arr2[0] = 0;
        arr2[1] = 1;
        assertArrayEquals(arr2, bag.getElems());
    }

    @Test
    void testcardinality() {
        Bag bag1 = new Bag();
        assertFalse(bag1.cardinality(1) > 0, "An empty bag can't contain an elem");
        bag1.add(1);
        bag1.add(2);
        assertTrue(bag1.cardinality(1) == 1, "The bag has one elem 1");
        assertTrue(bag1.cardinality(3) == 0, "The bag has no elem 3");
    }
}