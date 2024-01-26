import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class replaceTest {
    StringUtils stringUtils = new StringUtils();

    @Test
    void replace() {
        assertEquals("JusticeLeague",stringUtils.replace("JuzgiceLeague","zg","st"));
        assertEquals("TomAndJ", stringUtils.replace("TomAndJerry","Jerry","J"));
        assertEquals("OlongTee", stringUtils.replace("OolongTee","Oo","O"));
        assertEquals("TibbleIsJerrysNephew", stringUtils.replace("TibbleIsA","A","JerrysNephew"));
    }

    @Test
    void isReverse() {
        String a1 = "Bulbasaur";
        String b1 = "Jigglypuff";
        String a2 = "Snorlax";
        String b2 = "XalronS";
        String a3 = "Snorlax";
        String b3 = "xalronS";
        assertEquals(stringUtils.isReverse(a1, b1), false);
        assertEquals(stringUtils.isReverse(a2, b2), false);
        assertEquals(stringUtils.isReverse(a3, b3), true);

    }
}