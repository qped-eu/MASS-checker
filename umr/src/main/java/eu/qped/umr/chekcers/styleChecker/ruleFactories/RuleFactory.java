package eu.qped.umr.chekcers.styleChecker.ruleFactories;

/**
 *
 */

public abstract class RuleFactory {


    public static RuleFactory createXpathRuleMaker (){
        return new XpathRuleFactory();
    }

    public static RuleFactory createJavaRuleMaker (){
        return new JavaRuleFactory();
    }

}
