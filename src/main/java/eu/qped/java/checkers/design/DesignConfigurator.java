package eu.qped.java.checkers.design;

import eu.qped.java.checkers.mass.QFDesignSettings;

import java.util.ArrayList;

/**
 * Configuration Manager, manages and sets the DesignSettings for the DesignChecker and allows to be checked
 * against the settings.
 */
public class DesignConfigurator {

    private final QFDesignSettings qfDesignSettings;
    private ArrayList<ClassInfo> classInfos;
    private boolean modifierMaxRestrictive;


    public DesignConfigurator(QFDesignSettings qfDesignSettings) {
        this.qfDesignSettings = qfDesignSettings;
        setDefaultValues();
        if(qfDesignSettings != null) {
            setClassInfos(qfDesignSettings.getClassInfos());
            setModifierMaxRestrictive(qfDesignSettings.isModifierMaxRestrictive());
        }

    }

    public static DesignConfigurator createDesignConfigurator(QFDesignSettings qfDesignSettings) {
        return new DesignConfigurator(qfDesignSettings);
    }

    public static DesignConfigurator createDefaultDesignConfigurator () {
        return new DesignConfigurator(null);
    }

    /**
     * Default Values in case in the input has not been set
     */
    private void setDefaultValues() {
        setClassInfos(new ArrayList<>());
        setModifierMaxRestrictive(false);
    }

    public ArrayList<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(ArrayList<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }

    public boolean isModifierMaxRestrictive() {
        return modifierMaxRestrictive;
    }

    public void setModifierMaxRestrictive(boolean modifierMaxRestrictive) {
        this.modifierMaxRestrictive = modifierMaxRestrictive;
    }
}
