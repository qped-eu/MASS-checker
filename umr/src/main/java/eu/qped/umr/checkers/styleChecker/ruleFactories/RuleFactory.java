package eu.qped.umr.checkers.styleChecker.ruleFactories;

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
