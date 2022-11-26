package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QfClassSettings;

import java.util.ArrayList;

/**
 * Configuration Manager, manages and sets the DesignSettings for the DesignChecker and allows to be checked
 * against the settings.
 */
public class ClassConfigurator {

    private ArrayList<ClassInfo> classInfos;

    public ClassConfigurator(QfClassSettings qfClassSettings) {
        setDefaultValues();
        if (qfClassSettings != null) {
            setClassInfos(qfClassSettings.getClassInfos());
        }
    }

    /** create a ClassConfigurator object
     * @param qfClassSettings QfClassSettings object
     * @return a ClassConfigurator object
     */
    public static ClassConfigurator createClassConfigurator(QfClassSettings qfClassSettings) {
        return new ClassConfigurator(qfClassSettings);
    }

    /**
     * @return a null object from type ClassConfigurator
     */
    public static ClassConfigurator createDefaultClassConfigurator() {
        return new ClassConfigurator(null);
    }

    /**
     * Default Values in case in the input has not been set
     */
    private void setDefaultValues() {
        setClassInfos(new ArrayList<>());
    }

    /**
     * @return a list of all infos of the class
     */
    public ArrayList<ClassInfo> getClassInfos() {
        return classInfos;
    }

    /** enable the user to add info to the class
     * @param classInfos list of ClassInfo objects
     */
    public void setClassInfos(ArrayList<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }
}
