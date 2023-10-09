import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WordUtilsCustomTest {

    @Test
    public void test() {
        WordUtilsCustom wordUtilsCustom = new WordUtilsCustom();
        // Arrange
        String[] words = {"cherry"};
        char c = 'c'; // Testing with a

        // Act
        int result = wordUtilsCustom.countWordsBeginningWith(c, words);

        // Assert
        assertEquals(1, result); // There's only one word starting with 'A' (apple)
    }
}
