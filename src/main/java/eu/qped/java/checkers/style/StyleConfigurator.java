package eu.qped.java.checkers.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.QFStyleSettings;

/**
 * Style checker configs manager.
 * The class manage all the settings for the style checker and create a Setting Object to control the checker.
 *
 * @author Basel Alaktaa
 * @version 1.1
 * @since 01.06.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleConfigurator {


    private static final CheckLevel DEFAULT_CHECK_LEVEL = CheckLevel.BEGINNER;
	private int maxClassLength;
    private int maxMethodLength;
    private int maxFieldsCount;
    private int maxCycloComplexity;

    private String varNamesRegEx;
    private String methodNamesRegEx;
    private String classNameRegEx;


    private CheckLevel mainLevel;

    private CheckLevel namesLevel;
    private CheckLevel complexityLevel;
    private CheckLevel basisLevel;


    private  QFStyleSettings qfStyleSettings;



    private StyleConfigurator(QFStyleSettings qfStyleSettings) {
        this.qfStyleSettings = qfStyleSettings;
        this.readSettings();
    }

    public static StyleConfigurator createStyleConfigurator(QFStyleSettings qfStyleSettings) {
        return new StyleConfigurator(qfStyleSettings);
    }

    public static StyleConfigurator createDefaultStyleConfigurator () {
        return new StyleConfigurator(null);
    }



    protected void readSettings() {

        setDefaultValues();
        if (qfStyleSettings != null){
            if (qfStyleSettings.getBasisLevel() != null) {
                setBasisConfLevel(qfStyleSettings.getBasisLevel());
            }
            if (qfStyleSettings.getNamesLevel() != null){
                setNamesConfLevel(qfStyleSettings.getNamesLevel());
            }
            if (qfStyleSettings.getCompLevel() != null){
                setComplexityConfLevel(qfStyleSettings.getCompLevel());
            }
            if (qfStyleSettings.getClassLength() != null) {
                setMaxClassLength(Integer.parseInt(qfStyleSettings.getClassLength()));
            }
            if (qfStyleSettings.getMethodLength() != null){
                setMaxMethodLength(Integer.parseInt(qfStyleSettings.getMethodLength()));
            }
            if (qfStyleSettings.getFieldsCount() != null) {
                setMaxFieldsCount(Integer.parseInt(qfStyleSettings.getFieldsCount()));
            }
            if (qfStyleSettings.getCycloComplexity() != null){
                setMaxCycloComplexity(Integer.parseInt(qfStyleSettings.getCycloComplexity()));
            }
            if (qfStyleSettings.getVarName() != null) {
                setVarNamesRegEx(qfStyleSettings.getVarName());
            }
            if (qfStyleSettings.getMethodName() != null){
                setMethodNamesRegEx(qfStyleSettings.getMethodName());
            }
            if (qfStyleSettings.getClassName() != null) {
                setClassNameRegEx(qfStyleSettings.getClassName());
            }
        }
    }


    private void setDefaultValues() {

        this.setMaxClassLength(-1);
        this.setMaxMethodLength(-1);
        this.setMaxFieldsCount(-1);
        this.setMaxCycloComplexity(-1);
        this.setVarNamesRegEx("undefined");
        this.setClassNameRegEx("undefined");
        this.setMethodNamesRegEx("undefined");

        setMainLevel(CheckLevel.BEGINNER);

        setBasisLevel(CheckLevel.BEGINNER);

        setNamesLevel(CheckLevel.BEGINNER);

        setComplexityLevel(CheckLevel.BEGINNER);

    }


    private void setBasisConfLevel(String level) {
    	try {
    		setBasisLevel(CheckLevel.valueOf(level));
		} catch (IllegalArgumentException | NullPointerException e) {
			LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + DEFAULT_CHECK_LEVEL);
			setBasisLevel(DEFAULT_CHECK_LEVEL);
		}
    }

    private void setNamesConfLevel(String level) {
    	try {
    		setNamesLevel(CheckLevel.valueOf(level));
		} catch (IllegalArgumentException | NullPointerException e) {
			LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + DEFAULT_CHECK_LEVEL);
			setNamesLevel(DEFAULT_CHECK_LEVEL);
		}
    }

    private void setComplexityConfLevel(String level) {
    	try {
    		setComplexityLevel(CheckLevel.valueOf(level));
		} catch (IllegalArgumentException | NullPointerException e) {
			LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + DEFAULT_CHECK_LEVEL);
			setComplexityLevel(DEFAULT_CHECK_LEVEL);
		}
    }
}

