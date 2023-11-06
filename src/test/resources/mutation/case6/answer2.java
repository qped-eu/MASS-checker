import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class replaceTest {
    StringUtils stringUtils = new StringUtils();

    @Test
    void replace() {
        assertEquals("HaabiHaabo", stringUtils.replace("HalliHallo","ll","ab"));
        assertEquals("llllillllo", stringUtils.replace("HalliHallo","Ha","ll"));
        assertEquals("HalliHallServus", stringUtils.replace("HalliHallo","o","Servus"));
        assertEquals("mHallo", stringUtils.replace("HalliHallo","Halli","m"));

    }

    @Test
    void testIsReverseTrue() {
        Assertions.assertTrue(stringUtils.isReverse("abc", "cba"));
        Assertions.assertTrue(stringUtils.isReverse("1233hfa2", "2afh3321"));

    }

    @Test
    void testIsReverseFalse() {
        Assertions.assertFalse(stringUtils.isReverse("1234abc", "cba4322"));
        //Anbei weitere Tests für andere Szenarien!
        //Assertions.assertFalse(stringUtils.isReverse("1234abc", "5678xyz"));
        //Assertions.assertFalse(stringUtils.isReverse("starvinmarvin", "namtrac"));
    }

}