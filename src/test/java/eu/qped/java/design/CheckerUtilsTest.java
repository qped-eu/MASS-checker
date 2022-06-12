package eu.qped.java.design;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.design.CheckerUtils;
import eu.qped.java.checkers.design.infos.ExpectedElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerUtilsTest {

    private List<String> nonAccessMods;

    @BeforeEach
    public void setup() {
        nonAccessMods = new ArrayList<>();
    }

    @Test
    public void accessMatchTest() {
        assertTrue(CheckerUtils.isAccessMatch("public", "public"));
    }

    @Test
    public void optionalAccessMatchTest() {
        assertTrue(CheckerUtils.isAccessMatch("public", "*"));
    }

    @Test
    public void nonAccessMatchTest() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("abstract");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.abstractModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess));
    }

    @Test
    public void optionalNonAccessMatchTest() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("*");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.abstractModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess));
    }

    @Test
    public void extractClassTypeNameTest() {
        String classTypeName = "class HexaDecimal";
        nonAccessMods.add(CheckerUtils.EMPTY_MODIFIER);
        ExpectedElement elemInfo = new ExpectedElement(CheckerUtils.EMPTY_MODIFIER, nonAccessMods,  "class", "HexaDecimal");
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(classTypeName));
    }

    @Test
    public void extractFieldTest() {
        String classTypeName = "* abstract int number";
        nonAccessMods.add("abstract");
        ExpectedElement elemInfo = new ExpectedElement(CheckerUtils.OPTIONAL_KEYWORD, nonAccessMods,  "int", "number");
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(classTypeName));
    }

    @Test
    public void extractMethodTest() {
        String classTypeName = "public default void getName()";
        nonAccessMods.add("default");
        ExpectedElement elemInfo = new ExpectedElement("public", nonAccessMods,  "void", "getName()");
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(classTypeName));
    }

    @Test
    public void optionalOccurrenceTest() {
        String keyword = "* String *";
        assertEquals(2, CheckerUtils.countOptionalOccurrences(keyword));
    }
}
