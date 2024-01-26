import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Main m = new Main();

    @Test //1/2 bitte schreibt test für methoden und nicht bestimmte Fälle -1
    void testIsReverseTrue() { //void isReverse()...
        Assertions.assertTrue(m.isReverse("abc", "cba"));
        Assertions.assertTrue(m.isReverse("1233hfa2", "2afh3321"));

    }

    @Test
    void testIsReverseFalse() {
        Assertions.assertFalse(m.isReverse("1234abc", "cba4322"));
        //Anbei weitere Tests für andere Szenarien!
        //Assertions.assertFalse(m.isReverse("1234abc", "5678xyz"));
        //Assertions.assertFalse(m.isReverse("starvinmarvin", "namtrac"));
    }
}