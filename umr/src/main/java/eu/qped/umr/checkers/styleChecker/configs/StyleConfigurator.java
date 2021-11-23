package eu.qped.umr.checkers.styleChecker.configs;

import eu.qped.umr.model.CheckLevel;
import eu.qped.umr.qf.QFStyleSettings;

/**
 * Style checker configs manager.
 * The class manage all the settings for the style checker and create a Setting Object to control the checker.
 *
 * @author Basel Alaktaa
 * @version 1.1
 * @since 01.06.2021
 */
public class StyleConfigurator {


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


    private final QFStyleSettings qfStyleSettings;



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

        if (ConfiguratorUtility.getBegCodeWords().contains(level) && level != null) {
            setBasisLevel(CheckLevel.BEGINNER);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level) && level != null) {
            setBasisLevel(CheckLevel.ADVANCED);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level) && level != null) {
            setBasisLevel(CheckLevel.PROFESSIONAL);
        } else if (level == null) {
            setBasisLevel(CheckLevel.BEGINNER);
        } else {
            setBasisLevel(CheckLevel.BEGINNER);
        }
    }

    private void setNamesConfLevel(String level) {

        if (ConfiguratorUtility.getBegCodeWords().contains(level) && level != null) {
            setNamesLevel(CheckLevel.BEGINNER);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level) && level != null) {
            setNamesLevel(CheckLevel.ADVANCED);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level) && level != null) {
            setNamesLevel(CheckLevel.PROFESSIONAL);
        } else if (level == null) {
            setNamesLevel(CheckLevel.BEGINNER);
        } else {
            setNamesLevel(CheckLevel.BEGINNER);
        }
    }

    private void setComplexityConfLevel(String level) {
        if (ConfiguratorUtility.getBegCodeWords().contains(level) && level != null) {
            setComplexityLevel(CheckLevel.BEGINNER);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level) && level != null) {
            setComplexityLevel(CheckLevel.ADVANCED);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level) && level != null) {
            setComplexityLevel(CheckLevel.PROFESSIONAL);
        } else if (level == null) {
            setComplexityLevel(CheckLevel.BEGINNER);
        } else {
            setComplexityLevel(CheckLevel.BEGINNER);
        }
    }

    public CheckLevel getNamesLevel() {
        return namesLevel;
    }

    public void setNamesLevel(CheckLevel namesLevel) {
        this.namesLevel = namesLevel;
    }

    public CheckLevel getComplexityLevel() {
        return complexityLevel;
    }

    public void setComplexityLevel(CheckLevel complexityLevel) {
        this.complexityLevel = complexityLevel;
    }

    public CheckLevel getBasisLevel() {
        return basisLevel;
    }

    public void setBasisLevel(CheckLevel basisLevel) {
        this.basisLevel = basisLevel;
    }


    public int getMaxClassLength() {
        return maxClassLength;
    }

    public void setMaxClassLength(int maxClassLength) {
        this.maxClassLength = maxClassLength;
    }

    public int getMaxMethodLength() {
        return maxMethodLength;
    }

    public void setMaxMethodLength(int maxMethodLength) {
        this.maxMethodLength = maxMethodLength;
    }

    public int getMaxFieldsCount() {
        return maxFieldsCount;
    }

    public void setMaxFieldsCount(int maxFieldsCount) {
        this.maxFieldsCount = maxFieldsCount;
    }

    public int getMaxCycloComplexity() {
        return maxCycloComplexity;
    }

    public void setMaxCycloComplexity(int maxCycloComplexity) {
        this.maxCycloComplexity = maxCycloComplexity;
    }

    public String getVarNamesRegEx() {
        return varNamesRegEx;
    }

    public void setVarNamesRegEx(String varNamesRegEx) {
        this.varNamesRegEx = varNamesRegEx;
    }

    public String getMethodNamesRegEx() {
        return methodNamesRegEx;
    }

    public void setMethodNamesRegEx(String methodNamesRegEx) {
        this.methodNamesRegEx = methodNamesRegEx;
    }

    public String getClassNameRegEx() {
        return classNameRegEx;
    }

    public void setClassNameRegEx(String classNameRegEx) {
        this.classNameRegEx = classNameRegEx;
    }

    public CheckLevel getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(CheckLevel mainLevel) {
        this.mainLevel = mainLevel;
    }
}

