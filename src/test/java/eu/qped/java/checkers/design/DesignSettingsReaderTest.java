package eu.qped.java.checkers.design;

import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler;
import eu.qped.java.checkers.design.configuration.DesignSettings;
import eu.qped.java.checkers.design.configuration.MetricThreshold;
import eu.qped.java.checkers.mass.QFDesignSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents a test class for {@link DesignFeedback}.
 *
 * @author Jannik Seus
 */
class DesignSettingsReaderTest {

    private DesignSettings designSettings;

    @BeforeEach
    void setUp() {
        this.designSettings = DesignSettings.builder().build();
    }

    @Test
    void readDesignSettings() {
        QFDesignSettings qfDesignSettings = generateQfDesignSettings();

        designSettings.setAmc(new MetricThreshold(DesignCheckEntryHandler.Metric.AMC, Double.parseDouble(qfDesignSettings.getAmcMin()), Double.parseDouble(qfDesignSettings.getAmcMax())));
        assertEquals(designSettings.getAmc().getMinThreshold(), 0.5);
        assertEquals(designSettings.getAmc().getMaxThreshold(), 1.0);

        designSettings.setWmc(new MetricThreshold(DesignCheckEntryHandler.Metric.WMC, Double.parseDouble(qfDesignSettings.getWmcMin()), Double.parseDouble(qfDesignSettings.getWmcMax())));
        assertEquals(designSettings.getWmc().getMinThreshold(), 0.5);
        assertEquals(designSettings.getWmc().getMaxThreshold(), 1.0);

    }

    private QFDesignSettings generateQfDesignSettings() {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("0.5", "1.0");
        qfDesignSettings.setCa("0.5", "1.0");
        qfDesignSettings.setCam("0.5", "1.0");
        qfDesignSettings.setCbm("0.5", "1.0");
        qfDesignSettings.setCbo("0.5", "1.0");
        qfDesignSettings.setCc("0.5", "1.0");
        qfDesignSettings.setCe("0.5", "1.0");
        qfDesignSettings.setCis("0.5", "1.0");
        qfDesignSettings.setDam("0.5", "1.0");
        qfDesignSettings.setDit("0.5", "1.0");
        qfDesignSettings.setIc("0.5", "1.0");
        qfDesignSettings.setLcom("0.5", "1.0");
        qfDesignSettings.setLcom3("0.5", "1.0");
        qfDesignSettings.setLoc("0.5", "1.0");
        qfDesignSettings.setMoa("0.5", "1.0");
        qfDesignSettings.setMfa("0.5", "1.0");
        qfDesignSettings.setNoc("0.5", "1.0");
        qfDesignSettings.setNpm("0.5", "1.0");
        qfDesignSettings.setRfc("0.5", "1.0");
        qfDesignSettings.setWmc("0.5", "1.0");
        return qfDesignSettings;
    }
}