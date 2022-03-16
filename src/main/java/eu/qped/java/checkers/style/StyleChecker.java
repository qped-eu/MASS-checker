package eu.qped.java.checkers.style;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.style.pmd.PmdConfigException;
import eu.qped.java.checkers.style.pmd.RulesManager;
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

    private static final String MAIN_RULESET_PATH = "xmls/mainRuleset.xml";
    private transient final XmlFileManager xmlFileManager = XmlFileManager.createXmlFileManager();
    private transient StyleConfigurator styleConfigurator;

    private transient ArrayList<StyleFeedback> styleFeedbacks = new ArrayList<>();
    private ArrayList<StyleViolation> violations;



    public StyleChecker(final StyleConfigurator styleConfigurator) {
        this.styleConfigurator = styleConfigurator;
    }


    public void check() {

        if (styleConfigurator == null) {
            // create configurator with default settings
            styleConfigurator = StyleConfigurator.createDefaultStyleConfigurator();
        }

        ArrayList<CheckLevel> levels = new ArrayList<>();

        levels.add(styleConfigurator.getNamesLevel());
        levels.add(styleConfigurator.getComplexityLevel());
        levels.add(styleConfigurator.getBasisLevel());

        RulesManager rulesManager = RulesManager.createRulesManager(levels);

        try {
            rulesManager.prepareMainRuleset();
            applySettingsOnMainRuleset();
            executePMD();
            prepareFeedbacks();
        } catch (Exception e) {
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
        }
        finally {
            xmlFileManager.clearXmlFile();
        }
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


    private void applySettingsOnMainRuleset() {
        try {
            if (styleConfigurator.getMaxClassLength() != -1) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "ExcessiveClassLength", String.valueOf(styleConfigurator.getMaxClassLength()), "minimum");
            }
            if (styleConfigurator.getMaxMethodLength() != -1) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "ExcessiveMethodLength", String.valueOf(styleConfigurator.getMaxMethodLength()), "minimum");
            }
            if (styleConfigurator.getMaxFieldsCount() != -1) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "TooManyFields", String.valueOf(styleConfigurator.getMaxFieldsCount()), "maxfields");
            }
            if (styleConfigurator.getMaxCycloComplexity() != -1) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "CyclomaticComplexity", String.valueOf(styleConfigurator.getMaxCycloComplexity()), "methodReportLevel");
            }
            if (!styleConfigurator.getVarNamesRegEx().equals("undefined") && !styleConfigurator.getVarNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "LocalVariableNamingConventions", String.valueOf(styleConfigurator.getVarNamesRegEx()), "localVarPattern");
            }
            if (!styleConfigurator.getMethodNamesRegEx().equals("undefined") && !styleConfigurator.getMethodNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "MethodNamingConventions", String.valueOf(styleConfigurator.getMethodNamesRegEx()), "methodPattern");
            }
            if (!styleConfigurator.getClassNameRegEx().equals("undefined") && !styleConfigurator.getClassNameRegEx().equals("-1")) {
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "classPattern");
                xmlFileManager.editProperty(MAIN_RULESET_PATH, "ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "abstractClassPattern");
            }
        } catch (PmdConfigException e) {
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
            xmlFileManager.clearXmlFile();
        }
    }

    private void executePMD() {
        final PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("TestClass.java");
        configuration.setRuleSets(MAIN_RULESET_PATH);
        configuration.setReportFormat("json");
        configuration.setReportFile("report.json");
        PMD.doPMD(configuration);
    }

    public ArrayList<StyleFeedback> getStyleFeedbacks() {
        return styleFeedbacks;
    }

    public ArrayList<StyleViolation> getStyleViolationsList() {
        return this.violations;
    }
}
