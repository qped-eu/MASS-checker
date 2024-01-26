import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class LambdaCustomTest {

    @Test
    public void testRemoveIf() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        Predicate<Integer> evenPredicate = n -> n % 2 == 0;
        LambdaCustom.removeIf(numbers, evenPredicate);

        assertFalse(numbers.contains(2));
        assertFalse(numbers.contains(4));
        assertTrue(numbers.contains(5));
    }

    @Test
    public void testSortBy() {
        ArrayList<String> words = new ArrayList<>();
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        Comparator<String> lengthComparator = Comparator.comparing(String::length);
        LambdaCustom.sortBy(words, lengthComparator);

        assertEquals("apple", words.get(0));
        assertEquals("apple", words.get(1));
        assertEquals("apple", words.get(2));
        assertEquals("apple", words.get(3));
    }

    @Test
    public void testListToString() {
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(1.5);
        doubles.add(2.7);
        doubles.add(3.2);

        String result = LambdaCustom.listToString(doubles);

        assertEquals("1.5\n2.7\n3.2\n", result);
    }
}
