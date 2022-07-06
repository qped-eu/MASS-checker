package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.classdesign.enums.KeywordType;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerUtilsTest {

    private List<String> accessMods;
    private List<String> nonAccessMods;

    @BeforeEach
    public void setup() {
        accessMods = new ArrayList<>();
        nonAccessMods = new ArrayList<>();
    }

    @Test
    public void accessMatchTest() {
        accessMods.add("public");
        assertTrue(CheckerUtils.isAccessMatch("public", accessMods));
    }

    @Test
    public void nonAccessMatchTest() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("abstract");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.abstractModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess));
    }
}
