package eu.qped.java.checkers.style.analyse.pmd;

/**

 A utility class that provides constants for the PMD rule sets used by the code style analysis tool.
 The rule sets are stored as file paths relative to the project's resources directory.
 */
public final class PmdRulesSets {

    /**
     * The file path for the beginner-level naming convention rules set.
     */
    public final static String BEGINNER_NAMING_RULES_SET = "pmd-rulesets/namesBegRules.xml";
    /**
     * The file path for the advanced-level naming convention rules set.
     */
    public final static String ADVANCED_NAMING_RULES_SET = "pmd-rulesets/namesAdvRules.xml";
    /**
     * The file path for the professional-level naming convention rules set.
     */
    public final static String PROFESSIONAL_NAMING_RULES_SET = "pmd-rulesets/namesProRules.xml";

    /**
     * The file path for the beginner-level basic rules set.
     */
    public final static String BEGINNER_BASIC_RULES_SET = "pmd-rulesets/basicBegRules.xml";
    /**
     * The file path for the advanced-level basic rules set.
     */
    public final static String ADVANCED_BASIC_RULES_SET = "pmd-rulesets/basicAdvRules.xml";
    /**
     * The file path for the professional-level basic rules set.
     */
    public final static String PROFESSIONAL_BASIC_RULES_SET = "pmd-rulesets/basicProRules.xml";

    /**
     * The file path for the beginner-level complexity rules set.
     */
    public final static String BEGINNER_COMPLEXITY_RULES_SET = "pmd-rulesets/compAdvRules.xml";
    /**
     * The file path for the advanced-level complexity rules set.
     */
    public final static String ADVANCED_COMPLEXITY_RULES_SET = "pmd-rulesets/compAdvRules.xml";
    /**
     * The file path for the professional-level complexity rules set.
     */
    public final static String PROFESSIONAL_COMPLEXITY_RULES_SET = "pmd-rulesets/compProRules.xml";

    /**
     * The main rules set, which includes all of the rules from the other sets.
     */
    public final static String MAIN_RULES_SET = "";


}
