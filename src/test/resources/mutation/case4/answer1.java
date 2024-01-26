import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WordUtilsCustomTest {

    @Test
    public void test() {
        WordUtilsCustom wordUtilsCustom = new WordUtilsCustom();
        // Arrange
        String[] wordList = {"cherry"};
        String[] bigWordList = {"Cherry"};
        char c = 'c'; // Testing with c
        char bigC = 'C'; // Testing with big c

        String[] emptyList = {};

        // Assert
        assertEquals(1, wordUtilsCustom.countWordsBeginningWith(c, wordList)); 
      // assertEquals(1, wordUtilsCustom.countWordsBeginningWith('C', bigWordList));
    }
}
