package eu.qped.java.checkers.style;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.style.pmd.PmdConfigException;
import eu.qped.java.checkers.style.pmd.ViolationsFromReportParser;
import eu.qped.java.checkers.style.pmd.XmlFileManager;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;


/**
 * Checker class to check the code on Style violations
 * The style checker uses PMD-Core to do so
 * @author Basel Alaktaa
 * @since 08.5.2021
 * @version 1.4
 */
public class StyleChecker {

    private final XmlFileManager xmlFileManager = new XmlFileManager();
    private StyleConfigurator styleConfigurator;

    private ArrayList<StyleFeedback> styleFeedbacks = new ArrayList<>();
    private ArrayList<StyleViolation> violations;

    private String targetPath;



    public StyleChecker(final StyleConfigurator styleConfigurator) {
        this.styleConfigurator = styleConfigurator;
    }


    public void check() {

        if (styleConfigurator == null) {
            // create configurator with default settings
            styleConfigurator = StyleConfigurator.createDefaultStyleConfigurator();
        }

        addNameRules(styleConfigurator.getNamesLevel());
        addComplexityRules(styleConfigurator.getComplexityLevel());
        addBasicRules(styleConfigurator.getBasisLevel());
        applySettingsOnMainRuleset();
        executePMD();
        prepareFeedbacks();
    }


    private void prepareFeedbacks() {
        final ViolationsFromReportParser parser = ViolationsFromReportParser.createViolationsFromReportParser();

        violations = parser.parse();

        styleFeedbacks = new ArrayList<>();
        
        StyleFeedbackGenerator styleFeedbackGenerator = StyleFeedbackGenerator.createStyleFeedbackGenerator();
        for (StyleViolation styleViolation : this.violations){
            styleFeedbacks.add(new StyleFeedback(styleViolation.getDescription(),
                    styleFeedbackGenerator.getFeedbackBody(styleViolation.getRule()),
                    styleFeedbackGenerator.getFeedbackExample(styleViolation.getRule()),
                    "at Line: " + styleViolation.getLine()));
        }
    }

    /**
     * to add rules from a specific Rule set to the main Ruleset according to a level
     * @param level the level to determine the concrete Ruleset that we need
     */
    private void addNameRules (final CheckLevel level) {
    	if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesAdvRules.xml");
        }
        else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesProRules.xml");
        }
        else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesBegRules.xml");
        }
    }

    private void addComplexityRules(final CheckLevel level) {
    	if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compAdvRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compBegRules.xml");
        }
    }

    private void addBasicRules(final CheckLevel level) {
    	if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicAdvRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicBegRules.xml");
        }
    }
    
    private void applySettingsOnMainRuleset() {
        try {
            if (styleConfigurator.getMaxClassLength() != -1) {
                xmlFileManager.editProperty("ExcessiveClassLength", String.valueOf(styleConfigurator.getMaxClassLength()), "minimum");
            }
            if (styleConfigurator.getMaxMethodLength() != -1) {
                xmlFileManager.editProperty("ExcessiveMethodLength", String.valueOf(styleConfigurator.getMaxMethodLength()), "minimum");
            }
            if (styleConfigurator.getMaxFieldsCount() != -1) {
                xmlFileManager.editProperty("TooManyFields", String.valueOf(styleConfigurator.getMaxFieldsCount()), "maxfields");
            }
            if (styleConfigurator.getMaxCycloComplexity() != -1) {
                xmlFileManager.editProperty("CyclomaticComplexity", String.valueOf(styleConfigurator.getMaxCycloComplexity()), "methodReportLevel");
            }
            if (!styleConfigurator.getVarNamesRegEx().equals("undefined") && !styleConfigurator.getVarNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty("LocalVariableNamingConventions", String.valueOf(styleConfigurator.getVarNamesRegEx()), "localVarPattern");
            }
            if (!styleConfigurator.getMethodNamesRegEx().equals("undefined") && !styleConfigurator.getMethodNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty("MethodNamingConventions", String.valueOf(styleConfigurator.getMethodNamesRegEx()), "methodPattern");
            }
            if (!styleConfigurator.getClassNameRegEx().equals("undefined") && !styleConfigurator.getClassNameRegEx().equals("-1")) {
                xmlFileManager.editProperty("ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "classPattern");
                xmlFileManager.editProperty("ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "abstractClassPattern");
            }
        } catch (PmdConfigException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    private void executePMD() {
        final PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(targetPath);
        configuration.setRuleSets(xmlFileManager.getFilename());
        configuration.setReportFormat("json");
        configuration.setReportFile("report.json");
        PMD.runPmd(configuration);
    }

    public ArrayList<StyleFeedback> getStyleFeedbacks() {
        return styleFeedbacks;
    }

    public ArrayList<StyleViolation> getStyleViolationsList() {
        return this.violations;
    }
}
