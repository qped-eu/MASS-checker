import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class replaceTest {
    StringUtils stringUtils = new StringUtils();

    @Test
    void replace() {
        String source = "Hallo ich Sonne hier und ich Sonne da", search = "Sonne", replace = "bin";
        assertEquals(stringUtils.replace(source, search, replace), "Hallo ich bin hier und ich bin da");

        source = "Ich FFFFFFFFFFFFFFFFF am Akten FFFFFFFFFFFFFFFFFden";
        search = "FFFFFFFFFFFFFFFFF";
        replace = "bin";
        assertEquals(stringUtils.replace(source, search, replace), "Ich bin am Akten binden");

        source = "Ich mache jeden Tag und manchmal zwei Mal in mein Bett";
        search = "jeden Tag und manchmal zwei Mal";
        replace = "nie";
        assertEquals(stringUtils.replace(source, search, replace), "Ich mache nie in mein Bett");

        source = "Hallo Welt!";
        search = "Hunde";
        replace = "Katzen";
        assertEquals(stringUtils.replace(source, search, replace), "Hallo Welt!");

        source = "Meine Txstxtur ist kxput und ich kxnn kein x schreiben";
        search = "x";
        replace = "a";
        assertEquals(stringUtils.replace(source, search, replace), "Meine Tastatur ist kaput und ich kann kein a schreiben");

    }

}


