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

}