package eu.qped.java.style.analyse.pmd;

import org.junit.Assert;
import org.junit.Test;
import eu.qped.java.checkers.style.analyse.pmd.MassSupportedEditablePmdRules;

public class MassSupportedEditablePmdRulesTest {

    @Test
    public void testClassLength() {
        Assert.assertEquals("ExcessiveClassLength", MassSupportedEditablePmdRules.CLASS_LENGTH);
    }

    @Test
    public void testNamingConventions() {
        Assert.assertEquals("ClassNamingConventions", MassSupportedEditablePmdRules.CONVENTIONS);
        Assert.assertEquals("MethodNamingConventions", MassSupportedEditablePmdRules.CONVENTIONS1);
        Assert.assertEquals("LocalVariableNamingConventions", MassSupportedEditablePmdRules.CONVENTIONS2);
        Assert.assertEquals("MethodParameterNamingConventions", MassSupportedEditablePmdRules.CONVENTIONS3);
    }

    @Test
    public void testMethodLength() {
        Assert.assertEquals("ExcessiveMethodLength", MassSupportedEditablePmdRules.METHOD_LENGTH);
    }

    @Test
    public void testTooManyFields() {
        Assert.assertEquals("TooManyFields", MassSupportedEditablePmdRules.TOO_MANY_FIELDS);
    }

    @Test
    public void testCyclomaticComplexity() {
        Assert.assertEquals("CyclomaticComplexity", MassSupportedEditablePmdRules.COMPLEXITY);
    }
}
