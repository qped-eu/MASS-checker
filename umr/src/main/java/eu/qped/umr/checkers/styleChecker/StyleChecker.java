package eu.qped.umr.checkers.styleChecker;

import eu.qped.umr.checkers.MassChecker;
import eu.qped.umr.checkers.styleChecker.managers.FeedbackManager;
import eu.qped.umr.checkers.styleChecker.managers.XmlFileManager;
import eu.qped.umr.checkers.styleChecker.parsers.ViolationsFromReportParser;
import eu.qped.umr.helpers.Logger;
import eu.qped.umr.model.CheckLevel;
import eu.qped.umr.exceptions.NoSuchPropertyException;
import eu.qped.umr.exceptions.NoSuchRuleException;
import eu.qped.umr.exceptions.NoSuchRulesetException;
import eu.qped.umr.model.StyleFeedback;
import eu.qped.umr.model.StyleViolation;
import eu.qped.umr.checkers.styleChecker.configs.StyleConfigurator;
import eu.qped.umr.checkers.styleChecker.managers.RulesManager;

import net.sourceforge.pmd.*;
import net.sourceforge.pmd.PMDConfiguration;


import java.util.ArrayList;


/**
 * Checker class to check the code on Style violations
 * The style checker uses PMD-Core to do so
 * @author Basel Alaktaa
 * @since 08.5.2021
 * @version 1.4
 */
public class StyleChecker implements MassChecker {

    private static final Logger LOGGER = Logger.getInstance();

    private static final String MAIN_RULESET_PATH = "xmls/mainRuleset.xml";
    private transient final XmlFileManager xmlFileManager = XmlFileManager.createXmlFileManager();
    private transient StyleConfigurator styleConfigurator;

    private transient ArrayList<StyleFeedback> styleFeedbacks = new ArrayList<>();
    private ArrayList<StyleViolation> violations;



    protected StyleChecker(final StyleConfigurator styleConfigurator) {
        this.styleConfigurator = styleConfigurator;
    }


    @Override
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
            xmlFileManager.clearXmlFile();
        } catch (Exception e) {
            LOGGER.log(e.getMessage() + " " + e.getCause() + " class: " + e.getClass());
            xmlFileManager.clearXmlFile();
        }
    }


    private void prepareFeedbacks() {
        final ViolationsFromReportParser parser = ViolationsFromReportParser.createViolationsFromReportParser();

        violations = parser.parse();

        final FeedbackManager feedbackManager = FeedbackManager.createFeedbackManager(violations);
        feedbackManager.buildFeedback();
        styleFeedbacks = feedbackManager.getStyleFeedbacks();
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
        } catch (NoSuchRulesetException e) {
            LOGGER.log(e.getMessage() + " Ruleset is not found!");
            xmlFileManager.clearXmlFile();
        } catch (NoSuchPropertyException e) {
            LOGGER.log(e.getMessage() + e.getPropName() + " for the rule " + " in the ruleset " + e.getRuleset());
            xmlFileManager.clearXmlFile();
        } catch (NoSuchRuleException e) {
            LOGGER.log(e.getMessage() + e.getRuleName() + " in the RuleSet " + e.getXmlPath());
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
