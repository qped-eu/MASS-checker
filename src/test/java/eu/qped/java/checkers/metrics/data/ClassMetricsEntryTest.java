package eu.qped.java.checkers.metrics.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;

/**
 * Test class for {@link ClassMetricsEntry}.
 *
 * @author Jannik Seus
 */
class ClassMetricsEntryTest {

    private ClassMetricsEntry classMetricsEntry1;
    private ClassMetricsEntry classMetricsEntry2;
    private ClassMetricsEntry classMetricsEntry3;

    @BeforeEach
    void setUp() {
        classMetricsEntry1 = new ClassMetricsEntry("className", List.of());
        classMetricsEntry2 = new ClassMetricsEntry("", List.of());
        classMetricsEntry3 = new ClassMetricsEntry("anotherClass", List.of());
    }

    @Test
    void compareTo() {
        ArrayList<ClassMetricsEntry> entries = new ArrayList<>(List.of(classMetricsEntry1, classMetricsEntry2, classMetricsEntry3));
        entries.sort(Comparator.naturalOrder());
        List<ClassMetricsEntry> sortedEntries = List.of(classMetricsEntry2, classMetricsEntry3, classMetricsEntry1);
        assertArrayEquals(sortedEntries.toArray(), entries.toArray());
    }

    @Test
    void getClassName() throws NoSuchFieldException, IllegalAccessException {
        Field classNameField = classMetricsEntry1.getClass().getDeclaredField("className");
        classNameField.setAccessible(true);
        assertEquals("className", classNameField.get(classMetricsEntry1));
        assertEquals("className", classMetricsEntry1.getClassName());
        classNameField.setAccessible(false);

    }

    @Test
    void getMetricsForClass() throws NoSuchFieldException, IllegalAccessException {
        Field metricsForClassField = classMetricsEntry1.getClass().getDeclaredField("metricsForClass");
        metricsForClassField.setAccessible(true);
        Object[] expected = List.of().toArray();
        assertArrayEquals(expected, ((List<?>) metricsForClassField.get(classMetricsEntry1)).toArray());
        assertArrayEquals(expected, classMetricsEntry1.getMetricsForClass().toArray());
        metricsForClassField.setAccessible(false);
    }

}