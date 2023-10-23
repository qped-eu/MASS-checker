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

}


