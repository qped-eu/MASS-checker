package eu.qped.java.checkers.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.QfStyleSettings;
import eu.qped.java.checkers.style.pmd.MassSupportedEditablePmdRules;
import eu.qped.java.checkers.style.pmd.PmdConfigException;
import eu.qped.java.checkers.style.pmd.PmdRulesSets;
import eu.qped.java.checkers.style.pmd.XmlFileManager;
import eu.qped.java.checkers.style.reportModel.StyleCheckReport;
import eu.qped.java.checkers.style.settings.StyleConfigurationReader;
import eu.qped.java.checkers.style.settings.StyleSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;


/**
 * Checker class to check the code on Style violations
 * The style checker uses PMD-Core to do so
 *
 * @author Basel Alaktaa
 * @version 1.4
 * @since 08.5.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleChecker {

    private String targetPath;

    @Setter(AccessLevel.PACKAGE)
    private List<StyleFeedback> styleFeedbacks = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private StyleSettings styleSettings;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private QfStyleSettings qfStyleSettings;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private XmlFileManager xmlFileManager;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private StyleConfigurationReader styleConfigurationReader;

    public void check() {
        StyleConfigurationReader styleConfigurationReader = StyleConfigurationReader.builder().qfStyleSettings(qfStyleSettings).build();
        styleSettings = styleConfigurationReader.getStyleSettings();
        xmlFileManager = new XmlFileManager();
        addNameRules(styleSettings.getNamesLevel());
        addComplexityRules(styleSettings.getComplexityLevel());
        addBasicRules(styleSettings.getBasisLevel());
        applySettingsOnMainRuleset();
        executePMD();
        prepareFeedbacks();
    }


    private void prepareFeedbacks() {
        ReportFromJsonMapper mapper = ReportFromJsonMapper.builder().build();

        StyleCheckReport report = mapper.mapToReportObject();

        StyleFeedbackGenerator adapter = StyleFeedbackGenerator.builder().feedbacks(new HashMap<>()).report(report).build();

        //todo use this to generate a proper feedback.
        var resultFeedbacks = adapter.generateFeedbacks();

        //FIXME return the map instead
        styleFeedbacks = new ArrayList<>();
        for (Map.Entry<String, List<StyleFeedback>> entry : resultFeedbacks.entrySet()) {
            styleFeedbacks.addAll(entry.getValue());
        }
    }

    /**
     * to add rules from a specific Rule set to the main Ruleset according to a level
     *
     * @param level the level to determine the concrete Ruleset that we need
     */
    private void addNameRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.ADVANCED_NAMING_RULES_SET);
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.PROFESSIONAL_NAMING_RULES_SET);
        } else {
            xmlFileManager.addToMainRuleset(PmdRulesSets.BEGINNER_NAMING_RULES_SET);
        }
    }

    private void addComplexityRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.ADVANCED_COMPLEXITY_RULES_SET);
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.PROFESSIONAL_COMPLEXITY_RULES_SET);
        } else {
            xmlFileManager.addToMainRuleset(PmdRulesSets.BEGINNER_COMPLEXITY_RULES_SET);
        }
    }

    private void addBasicRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.ADVANCED_BASIC_RULES_SET);
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset(PmdRulesSets.PROFESSIONAL_BASIC_RULES_SET);
        } else {
            xmlFileManager.addToMainRuleset(PmdRulesSets.BEGINNER_BASIC_RULES_SET);
        }
    }

    private void applySettingsOnMainRuleset() {
        try {
            if (styleSettings.getMaxClassLength() != -1) {
                xmlFileManager.editProperty(
                        MassSupportedEditablePmdRules.EXCESSIVE_CLASS_LENGTH, String.valueOf(styleSettings.getMaxClassLength()
                        ), "minimum");
            }
            if (styleSettings.getMaxMethodLength() != -1) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.EXCESSIVE_METHOD_LENGTH, String.valueOf(styleSettings.getMaxMethodLength()), "minimum");
            }
            if (styleSettings.getMaxFieldsCount() != -1) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.TOO_MANY_FIELDS, String.valueOf(styleSettings.getMaxFieldsCount()), "maxfields");
            }
            if (styleSettings.getMaxCycloComplexity() != -1) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.CYCLOMATIC_COMPLEXITY, String.valueOf(styleSettings.getMaxCycloComplexity()), "methodReportLevel");
            }
            if (!styleSettings.getVarNamesRegEx().equals("undefined") && !styleSettings.getVarNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.LOCAL_VARIABLE_NAMING_CONVENTIONS, String.valueOf(styleSettings.getVarNamesRegEx()), "localVarPattern");
            }
            if (!styleSettings.getMethodParameterNamesRegEx().equals("undefined")) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.METHOD_PARAMETER_NAMING_CONVENTIONS, String.valueOf(styleSettings.getMethodParameterNamesRegEx()), "methodParameterPattern");
            }
            if (!styleSettings.getMethodParameterNamesRegEx().equals("undefined")) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.METHOD_PARAMETER_NAMING_CONVENTIONS, String.valueOf(styleSettings.getMethodParameterNamesRegEx()), "finalMethodParameterPattern");
            }
            if (!styleSettings.getMethodNamesRegEx().equals("undefined") && !styleSettings.getMethodNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.METHOD_NAMING_CONVENTIONS, String.valueOf(styleSettings.getMethodNamesRegEx()), "methodPattern");
            }
            if (!styleSettings.getClassNameRegEx().equals("undefined") && !styleSettings.getClassNameRegEx().equals("-1")) {
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.CLASS_NAMING_CONVENTIONS, String.valueOf(styleSettings.getClassNameRegEx()), "classPattern");
                xmlFileManager.editProperty(MassSupportedEditablePmdRules.CLASS_NAMING_CONVENTIONS, String.valueOf(styleSettings.getClassNameRegEx()), "abstractClassPattern");
            }
        } catch (PmdConfigException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }


    private void executePMD() {
        final PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(targetPath);
        configuration.setRuleSets(xmlFileManager.getFilename());
        configuration.setReportFormat("json");
        configuration.setReportFile("src/main/java/eu/qped/java/checkers/style/resources/report.json");
        PMD.runPmd(configuration);
    }

}
