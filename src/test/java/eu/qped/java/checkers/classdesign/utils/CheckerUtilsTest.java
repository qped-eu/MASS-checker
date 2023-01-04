package eu.qped.java.checkers.classdesign.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.javaparser.ast.Modifier;

import eu.qped.java.checkers.classdesign.CheckerUtils;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

public class CheckerUtilsTest {


    @Test
    public void extractElementTest() {
        FieldKeywordConfig field = new FieldKeywordConfig();
        field.setPublicModifier(KeywordChoice.YES.toString());
        field.setFinalModifier(KeywordChoice.YES.toString());
        field.setType("int");
        field.setName("a;");
        field.setAllowExactModifierMatching(false);

        ExpectedElement elemInfo;
        try {
            elemInfo = CheckerUtils.extractExpectedInfo(field);
            ExpectedElement expectedElement = new ExpectedElement(
                    Collections.singletonList("public"),
                    Collections.singletonList("final"),
                    Collections.singletonList("int"),
                    "a",
                    false, false);

            assertEquals(expectedElement, elemInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void accessMatchTest() {
        assertTrue(CheckerUtils.isAccessMatch("public", Collections.singletonList("public")));
    }

    @Test
    public void nonAccessExactMatch() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("final");
        expectedNonAccess.add("static");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.staticModifier());
        presentModifiers.add(Modifier.finalModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess, true, true));
    }

    @Test
    public void nonAccessNotExactMatch() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("final");
        expectedNonAccess.add("static");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.staticModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess, false, false));
    }
}
