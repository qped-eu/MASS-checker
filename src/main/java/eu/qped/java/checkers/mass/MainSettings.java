package eu.qped.java.checkers.mass;


import eu.qped.framework.CheckLevel;
import eu.qped.java.utils.SupportedLanguages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

/**
 * class to read the Setting from Quarterfall and
 * set defaults for missing inputs
 *
 * @author Basel Alaktaa and Mayar Hamdash
 * @version 1.0
 * @since 15.06.2021 18:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainSettings {

    private CheckLevel syntaxLevel;
    private String preferredLanguage;
    private boolean styleNeeded;
    private boolean semanticNeeded;
    private boolean metricsNeeded;
    private boolean classNeeded;
    private boolean coverageNeeded;
    private boolean mutationNeeded;

    private QfMainSettings qfMainSettings;
    private QfMass mass;


    public MainSettings(QfMainSettings qfMainSettings) {
        this.qfMainSettings = qfMainSettings;
        readSettingsFromQfMainSettings();
    }

    public MainSettings(QfMass mass, String preferredLanguage) {
        this.mass = mass;
        this.preferredLanguage = preferredLanguage;
        readSettingsFromMass();
    }

    private void readSettingsFromMass() {
        this.setMetricsNeeded(mass.isMetricsSelected());
        this.setStyleNeeded(mass.isStyleSelected());
        this.setSemanticNeeded(mass.isSemanticSelected());
        this.setCoverageNeeded(mass.isCoverageSelected());
        this.setClassNeeded(mass.isClassSelected());
        this.setMutationNeeded(mass.isMutationSelected());
        boolean isLanguageApplicable = this.preferredLanguage != null && !"".equals(this.preferredLanguage);
        this.setPreferredLanguage(isLanguageApplicable ? preferredLanguage : SupportedLanguages.ENGLISH);
        var syntaxLevel = extractSyntaxLevel(this.mass.getSyntax().getLevel());
        this.setSyntaxLevel(syntaxLevel);

    }

    private CheckLevel extractSyntaxLevel(String level) {
        try {
            return CheckLevel.valueOf(level);
        } catch (IllegalArgumentException e) {
            return CheckLevel.BEGINNER;
        }
    }

    private void readSettingsFromQfMainSettings() {
        classNeeded = qfMainSettings.getClassNeeded() != null && Boolean.parseBoolean(qfMainSettings.getClassNeeded());
        styleNeeded = qfMainSettings.getStyleNeeded() != null && Boolean.parseBoolean(qfMainSettings.getStyleNeeded());
        semanticNeeded = qfMainSettings.getSemanticNeeded() != null && Boolean.parseBoolean(qfMainSettings.getSemanticNeeded());
        metricsNeeded = qfMainSettings.getMetricsNeeded() != null && Boolean.parseBoolean(qfMainSettings.getMetricsNeeded());
        syntaxLevel = qfMainSettings.getSyntaxLevel() != null ? getSyntaxConfLevel(qfMainSettings.getSyntaxLevel()) : CheckLevel.BEGINNER;
        preferredLanguage = qfMainSettings.getPreferredLanguage() != null ? qfMainSettings.getPreferredLanguage() : "en";
    }


    private CheckLevel getSyntaxConfLevel(String level) {
        CheckLevel checkLevel = CheckLevel.BEGINNER;
        try {
            checkLevel = CheckLevel.valueOf(level);
        } catch (IllegalArgumentException | NullPointerException e) {
            LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + checkLevel);
        }
        return checkLevel;
    }
}
