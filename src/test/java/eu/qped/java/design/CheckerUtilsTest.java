package eu.qped.java.design;

import eu.qped.java.checkers.design.CheckerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckerUtilsTest {

    private List<String> expectedKeywords;

    @BeforeEach
    public void setup() {
        expectedKeywords = new ArrayList<>();
    }

    @Test
    public void extractClassTypeNameTest() {
        String classTypeName = "public class HexaDecimal";
        assertArrayEquals(new String[] {"public", "",  "class", "HexaDecimal"}, CheckerUtils.extractClassNameInfo(classTypeName));
    }

    @Test
    public void optionalOccurrenceTest() {
        String keyword = "* String *";
        assertEquals(2, CheckerUtils.countOptionalOccurrences(keyword));
    }

    @Test
    public void extractOptionalAccessModifierTest() {
        String keywords = "* final static String name;";
        expectedKeywords.add(keywords);
        assertEquals("*", CheckerUtils.getAccessModifiersFromString(expectedKeywords).get(0));
    }

    @Test
    public void extractAccessModifierTest() {
        String keywords = "public final static String name;";
        expectedKeywords.add(keywords);
        assertEquals("public", CheckerUtils.getAccessModifiersFromString(expectedKeywords).get(0));
    }

    @Test
    public void extractOptionalNonAccessModifierTest() {
        String keywords = "* String name;";
        expectedKeywords.add(keywords);
        String result = String.join(" ", CheckerUtils.getNonAccessModifiersFromString(expectedKeywords).get(0));
        assertEquals("*", result);
    }

    @Test
    public void extractNonAccessModifierTest() {
        String keywords = "final static String name;";
        expectedKeywords.add(keywords);
        String result = String.join(" ", CheckerUtils.getNonAccessModifiersFromString(expectedKeywords).get(0));
        assertEquals("final static", result);
    }

    @Test
    public void extractOptionalTypeTest() {
        String keywords = "* name;";
        expectedKeywords.add(keywords);
        assertEquals("*", CheckerUtils.getElementType(expectedKeywords).get(0));
    }

    @Test
    public void extractTypeTest() {
        String keywords = "String name;";
        expectedKeywords.add(keywords);
        assertEquals("String", CheckerUtils.getElementType(expectedKeywords).get(0));
    }

    @Test
    public void extractOptionalNameTest() {
        String keywords = "*";
        expectedKeywords.add(keywords);
        assertEquals("*", CheckerUtils.getExpectedElementName(expectedKeywords).get(0));
    }

    @Test
    public void extractNameTest() {
        String keywords = "name;";
        expectedKeywords.add(keywords);
        assertEquals("name", CheckerUtils.getExpectedElementName(expectedKeywords).get(0));
    }
}
