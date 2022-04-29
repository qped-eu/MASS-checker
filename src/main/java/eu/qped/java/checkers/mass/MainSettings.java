package eu.qped.java.checkers.mass;



import java.util.Map;

import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;

/**
 * class to read the Setting from Quarterfall and
 * set defaults for missing inputs
 * @author Basel Alaktaa and Mayar Hamdash
 * @version 1.0
 * @since 15.06.2021 18:33
 */
public class MainSettings {
    private static final CheckLevel DEFAULT_CHECK_LEVEL = CheckLevel.BEGINNER;
	private CheckLevel syntaxLevel;
    private String preferredLanguage;
    private String runStyle;
    private String semanticNeeded;

    private boolean styleNeeded;

    private Map<String, String> settings;
    private QFMainSettings qfMainSettings;


    private MainSettings(QFMainSettings qfMainSettings) {
        this.qfMainSettings = qfMainSettings;
        setUpDefaults();
        readSettings();

    }

    public static MainSettings createMainSettingsConfigurator(QFMainSettings qfMainSettings) {
        return new MainSettings(qfMainSettings);
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

    	if (!settings.containsKey("syntaxLevel"))
    		settings.put("syntaxLevel", DEFAULT_CHECK_LEVEL.name());
    	if (!settings.containsKey("preferredLanguage"))
    		settings.put("preferredLanguage", "en");
    	if (!settings.containsKey("styleNeeded"))
    		settings.put("styleNeeded", "true");
    	if (!settings.containsKey("semanticNeeded"))
    		settings.put("semanticNeeded", "false");
    }


    public MainSettings(Map<String, String> settings) {
        this.settings = settings;
        this.setUpDefaults();
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
    	try {
			setSyntaxLevel(CheckLevel.valueOf(level));
		} catch (IllegalArgumentException | NullPointerException e) {
			LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + DEFAULT_CHECK_LEVEL);
			setSyntaxLevel(DEFAULT_CHECK_LEVEL);
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
