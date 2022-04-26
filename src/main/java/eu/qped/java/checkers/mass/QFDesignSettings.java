package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.design.ClassInfo;

import java.util.ArrayList;

public class QFDesignSettings extends QfObjectBase {

    private ArrayList<ClassInfo> classInfos;

    private boolean modifierMaxRestrictive;

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
