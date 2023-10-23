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

}