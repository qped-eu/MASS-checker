import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    StringUtils stringUtils = new StringUtils();

    @org.junit.jupiter.api.Test
    void test() {
        assertEquals("Nice guys! Nice guys! Nice guys!", stringUtils.replace("Nice people! Nice people! Nice people!", "people", "guys"));
        assertEquals("Hey Freunde!", stringUtils.replace("Hey Freunde!", "3210", "tango"));
        assertEquals("Super Freunde!", stringUtils.replace("Hey Freunde!", "Hey", "Super"));
        assertEquals("Hey Freunde?", stringUtils.replace("Hey Freunde!", "!", "?"));

    }

    @org.junit.jupiter.api.Test
    void isReverse() {
        assertEquals(true, stringUtils.isReverse("XYZ", "ZYX"));
        assertEquals(true, stringUtils.isReverse("FDRMTZL", "LZTMRDF"));
        assertEquals(true, stringUtils.isReverse("Make better", "retteb ekaM"));
        assertEquals(true, stringUtils.isReverse("STaR WaRs", "sRaW RaTS"));
    }
}