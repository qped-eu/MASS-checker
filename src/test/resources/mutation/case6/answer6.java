import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class replaceTest {
    StringUtils stringUtils = new StringUtils();

    @Test
    void replace() {
        String firstResult = stringUtils.replace("Hallo/Viktoria,/ich/wuensche/dir/einen/angenehmen/Tag.", "/", " ");
        assertEquals("Hallo Viktoria, ich wuensche dir einen angenehmen Tag.", firstResult);

        String secondResult = stringUtils.replace("!?I!?ch ma!?g di!?es!?en Ku!?rs!?.", "!?", "");
        assertEquals("Ich mag diesen Kurs.", secondResult);

        String thirdResult = stringUtils.replace("Iron Man ist ein Superheld. Iron Man ist besonders stark. Wer mag Iron Man nicht?", "Iron Man", "Hulk");
        assertEquals("Hulk ist ein Superheld. Hulk ist besonders stark. Wer mag Hulk nicht?", thirdResult);

    }

    @Test
    void isReverse() {
        boolean firstResult = stringUtils.isReverse("Tobias", "Viktoria");
        assertFalse(firstResult);

        boolean secondResult = stringUtils.isReverse("Otto", "otto");
        assertFalse(secondResult);

        boolean thirdResult = stringUtils.isReverse("otto", "otto");
        assertTrue(thirdResult);

        boolean fourthResult = stringUtils.isReverse("Tobias", "saiboT");
        assertTrue(fourthResult);

        boolean fifthResult = stringUtils.isReverse(" ! § $ % & / ( ) = ", " = ) ( / & % $ § ! ");
        assertTrue(fifthResult);
    }

}


