package eu.qped.java.checkers.classdesign.infos;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

class ExpectedElementTest {

    @Test
    public void testIsExactMatch() {
        List<String> accessModifiers = Arrays.asList("public");
        List<String> nonAccessModifiers = Arrays.asList("static");
        List<String> types = Arrays.asList("int");
        String name = "count";
        boolean isExactMatch = true;
        boolean containsYes = false;

        ExpectedElement expectedElement = new ExpectedElement(accessModifiers, nonAccessModifiers, types, name, isExactMatch, containsYes);
        assertTrue(expectedElement.isExactMatch());
    }

    @Test
    public void testIsContainsYes() {
        List<String> accessModifiers = Arrays.asList("public");
        List<String> nonAccessModifiers = Arrays.asList("static");
        List<String> types = Arrays.asList("int");
        String name = "count";
        boolean isExactMatch = true;
        boolean containsYes = true;

        ExpectedElement expectedElement = new ExpectedElement(accessModifiers, nonAccessModifiers, types, name, isExactMatch, containsYes);
        assertTrue(expectedElement.isContainsYes());
    }

    @Test
    public void testGetPossibleAccessModifiers() {
        List<String> accessModifiers = Arrays.asList("public");
        List<String> nonAccessModifiers = Arrays.asList("static");
        List<String> types = Arrays.asList("int");
        String name = "count";
        boolean isExactMatch = true;
        boolean containsYes = false;

        ExpectedElement expectedElement = new ExpectedElement(accessModifiers, nonAccessModifiers, types, name, isExactMatch, containsYes);
        List<String> actualAccessModifiers = expectedElement.getPossibleAccessModifiers();
        assertArrayEquals(accessModifiers.toArray(), actualAccessModifiers.toArray());
    }

    @Test
    public void testGetPossibleNonAccessModifiers() {
        List<String> accessModifiers = Arrays.asList("public");
        List<String> nonAccessModifiers = Arrays.asList("static");
        List<String> types = Arrays.asList("int");
        String name = "count";
        boolean isExactMatch = true;
        boolean containsYes = false;

        ExpectedElement expectedElement = new ExpectedElement(accessModifiers, nonAccessModifiers, types, name, isExactMatch, containsYes);
        List<String> actualNonAccessModifiers = expectedElement.getPossibleNonAccessModifiers();
        assertArrayEquals(nonAccessModifiers.toArray(), actualNonAccessModifiers.toArray());
    }
}