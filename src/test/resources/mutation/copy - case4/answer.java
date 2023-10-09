import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class LambdaTest {

     @Test
    public void testSortBy() {
        ArrayList<String> words = new ArrayList<>();
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");
        words.add("apple");

        Comparator<String> lengthComparator = Comparator.comparing(String::length);
        Lambda.sortBy(words, lengthComparator);

        assertEquals("apple", words.get(0));
        assertEquals("apple", words.get(1));
        assertEquals("apple", words.get(2));
        assertEquals("apple", words.get(3));
    }

	
}
