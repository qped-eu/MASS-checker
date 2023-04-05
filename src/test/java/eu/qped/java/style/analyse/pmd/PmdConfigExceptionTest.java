package eu.qped.java.style.analyse.pmd;

import static org.junit.Assert.assertEquals;

import eu.qped.java.checkers.style.analyse.pmd.PmdConfigException;
import org.junit.Test;

public class PmdConfigExceptionTest {

    @Test
    public void testConstructorWithRuleNameAndPropName() {
        // Given
        String ruleName = "SomeRule";
        String propName = "SomeProp";

        // When
        PmdConfigException exception = new PmdConfigException(ruleName, propName);

        // Then
        String expectedMessage = "Element not found (ruleName: SomeRule, propName: SomeProp)";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(ruleName, exception.getRuleName());
        assertEquals(propName, exception.getPropName());
    }

    @Test
    public void testConstructorWithNullRuleName() {
        // Given
        String propName = "SomeProp";

        // When
        PmdConfigException exception = new PmdConfigException(null, propName);

        // Then
        String expectedMessage = "ruleName must not be null (ruleName: null, propName: SomeProp)";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(null, exception.getRuleName());
        assertEquals(propName, exception.getPropName());
    }

    @Test
    public void testConstructorWithNullPropName() {
        // Given
        String ruleName = "SomeRule";

        // When
        PmdConfigException exception = new PmdConfigException(ruleName, null);

        // Then
        String expectedMessage = "propertyName must not be null (ruleName: SomeRule, propName: null)";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(ruleName, exception.getRuleName());
        assertEquals(null, exception.getPropName());
    }

    @Test
    public void testConstructorWithCustomMessage() {
        // Given
        String message = "Something went wrong";
        String ruleName = "SomeRule";
        String propName = "SomeProp";

        // When
        PmdConfigException exception = new PmdConfigException(message, ruleName, propName);

        // Then
        String expectedMessage = "Something went wrong (ruleName: SomeRule, propName: SomeProp)";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(ruleName, exception.getRuleName());
        assertEquals(propName, exception.getPropName());
    }

}
