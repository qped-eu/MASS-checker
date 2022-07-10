package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.framework.Checker;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.enums.KeywordType;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerUtilsTest {


    @Test
    public void extractElementTest() {
        FieldKeywordConfig field = new FieldKeywordConfig();
        field.setPublicModifier(KeywordChoice.YES.toString());
        field.setFinalModifier(KeywordChoice.YES.toString());
        field.setType("int");
        field.setName("a");
        field.setAllowExactModifierMatching("false");

        ExpectedElement elemInfo = CheckerUtils.extractExpectedInfo(field);
        ExpectedElement expectedElement = new ExpectedElement(
                Collections.singletonList("public"),
                Collections.singletonList("final"),
                Collections.singletonList("int"),
                "a",
                false, false);

        assertEquals(expectedElement, elemInfo);
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
