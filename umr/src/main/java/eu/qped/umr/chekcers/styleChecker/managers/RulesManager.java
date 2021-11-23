package eu.qped.umr.chekcers.styleChecker.managers;


import eu.qped.umr.model.CheckLevel;

import java.util.ArrayList;

public class RulesManager {

    private final ArrayList<CheckLevel> levels;
    private final XmlFileManager xmlFileManager = XmlFileManager.createXmlFileManager();


    protected RulesManager(ArrayList<CheckLevel> levels ){
        this.levels = levels;

    }
    public static RulesManager createRulesManager(ArrayList<CheckLevel> levels ){
        return new RulesManager(levels);
    }

    public void prepareMainRuleset() {
        addNameRules(levels.get(0));
        addComplexityRules(levels.get(1));
        addBasicRules(levels.get(2));
    }

    /**
     * to add rules from a specific Rule set to the main Ruleset according to a level
     * @param level the level to determine the concrete Ruleset that we need
     */
    private void addNameRules (final CheckLevel level) {
        if (level.equals(CheckLevel.BEGINNER)) {
            xmlFileManager.addToMainRuleset("xmls/namesBegRules.xml");
        }
        else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("xmls/namesAdvRules.xml");
        }
        else if (level.equals(CheckLevel.PROFESSIONAL)) {
            xmlFileManager.addToMainRuleset("xmls/namesProRules.xml");
        }
        else {
            xmlFileManager.addToMainRuleset("xmls/namesBegRules.xml");
        }
    }

    private void addComplexityRules(final CheckLevel level) {
        if (level.equals(CheckLevel.BEGINNER)) {
            xmlFileManager.addToMainRuleset("xmls/compBegRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("xmls/compAdvRules.xml");
        } else if (level.equals(CheckLevel.PROFESSIONAL)) {
            xmlFileManager.addToMainRuleset("xmls/compProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("xmls/compBegRules.xml");
        }
    }

    private void addBasicRules(final CheckLevel level) {
        if (level.equals(CheckLevel.BEGINNER)) {
            xmlFileManager.addToMainRuleset("xmls/basicBegRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("xmls/basicAdvRules.xml");
        } else if (level.equals(CheckLevel.PROFESSIONAL)) {
            xmlFileManager.addToMainRuleset("xmls/basicProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("xmls/basicBegRules.xml");
        }
    }
}
