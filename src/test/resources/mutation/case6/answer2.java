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

}