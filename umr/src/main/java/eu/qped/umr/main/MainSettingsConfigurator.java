package eu.qped.umr.main;



import eu.qped.umr.model.CheckLevel;
import eu.qped.umr.checkers.styleChecker.configs.ConfiguratorUtility;
import eu.qped.umr.qf.QFMainSettings;

import java.util.Map;

/**
 * class to read the Setting from Quarterfall and
 * set defaults for missing inputs
 * @author Basel Alaktaa and Mayar Hamdash
 * @version 1.0
 * @since 15.06.2021 18:33
 */
public class MainSettingsConfigurator {
    private CheckLevel syntaxLevel;
    private String preferredLanguage;
    private String runStyle;
    private String semanticNeeded;

    private boolean styleNeeded;

    private Map<String, String> settings;
    private QFMainSettings qfMainSettings;


    private MainSettingsConfigurator(QFMainSettings qfMainSettings) {
        this.qfMainSettings = qfMainSettings;
        setUpDefaults();
        readSettings();

    }

    public static MainSettingsConfigurator createMainSettingsConfigurator(QFMainSettings qfMainSettings) {
        return new MainSettingsConfigurator(qfMainSettings);
    }


    public void readSettings () {
        if (qfMainSettings.getStyleNeeded() != null) {
            setStyleNeeded(Boolean.parseBoolean(qfMainSettings.getStyleNeeded()));
        }
        if (qfMainSettings.getPreferredLanguage() != null) {
            setPreferredLanguage(qfMainSettings.getPreferredLanguage());
        }
    }
    private void setUpDefaults() {

        settings.put("syntaxLevel", "beg");
        settings.put("preferredLanguage", "en");
        settings.put("styleNeeded", "true");
        settings.put("semanticNeeded", "false");
    }


    public MainSettingsConfigurator(Map<String, String> settings) {
        this.settings = settings;
        if (settings == null){
            this.setUpDefaults();
        }
        if (settings != null){
            this.setUp();
        }
    }



    private void setUp() {
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            if (entry.getKey() != null) {
                switch (entry.getKey()) {
                    case "syntaxLevel": {
                        if (entry.getValue() != null) {
                            this.setSyntaxConfLevel(entry.getValue());
                        }
                        break;
                    }
                    case "preferredLanguage": {
                        if (entry.getValue() != null) {
                            this.setPreferredLanguage( entry.getValue());
                        }
                        break;
                    }
                    case "styleNeeded": {
                        if (entry.getValue() != null) {
                            this.setRunStyle(entry.getValue());
                        }
                        break;
                    }
                    case "semanticNeeded": {
                        if (entry.getValue() != null) {
                            this.setSemanticNeeded(entry.getValue());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void setSyntaxConfLevel(String level) {
        if (ConfiguratorUtility.getBegCodeWords().contains(level)) {
            setSyntaxLevel(CheckLevel.BEGINNER);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level)) {
            setSyntaxLevel(CheckLevel.ADVANCED);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level)) {
            setSyntaxLevel(CheckLevel.PROFESSIONAL);
        } else if(level == null) {
            setSyntaxLevel(CheckLevel.BEGINNER);
        }
        else {
            setSyntaxLevel(CheckLevel.BEGINNER);
        }
    }


    public CheckLevel getSyntaxLevel() {
        return syntaxLevel;
    }

    public void setSyntaxLevel(CheckLevel syntaxLevel) {
        this.syntaxLevel = syntaxLevel;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getRunStyle() {
        return runStyle;
    }

    public void setRunStyle(String runStyle) {
        this.runStyle = runStyle;
    }

    public String getSemanticNeeded() {
        return semanticNeeded;
    }

    public void setSemanticNeeded(String semanticNeeded) {
        this.semanticNeeded = semanticNeeded;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public boolean isStyleNeeded() {
        return styleNeeded;
    }

    public void setStyleNeeded(boolean styleNeeded) {
        this.styleNeeded = styleNeeded;
    }
}
