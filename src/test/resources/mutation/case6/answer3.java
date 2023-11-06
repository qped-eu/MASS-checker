import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class replaceTest {
    StringUtils stringUtils = new StringUtils();

    @Test
    void replace() {
        Assertions.assertEquals("HaXXo", stringUtils.replace("Hallo", "l", "X"));
        Assertions.assertEquals("regal", stringUtils.replace("Lagerregal", "Lager", ""));
        Assertions.assertEquals("Xallo", stringUtils.replace("Hallo", "H", "X"));
        Assertions.assertEquals("Buch", stringUtils.replace("Buchband", "band", ""));
        Assertions.assertEquals("Buchrücken", stringUtils.replace("Buchband", "band", "rücken"));

    }

    @Test
    void isReverse() {
        Assertions.assertEquals(true, stringUtils.isReverse("Hallo", "ollaH")); // gespiegelt
        Assertions.assertEquals(false, stringUtils.isReverse("Halloo", "ollaH")); // nicht gleiche Länge
        Assertions.assertEquals(false, stringUtils.isReverse("Halloo", "ololaH")); // gleiche Buchstaben aber falsche Reihenfolge
        Assertions.assertEquals(false, stringUtils.isReverse("Hallo", "Hallo")); // zwei mal die gleiche Reihenfolge
        Assertions.assertEquals(true, stringUtils.isReverse("ollah", "hallo")); // doppelte spiegelung

    }

}