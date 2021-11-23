package eu.qped.umr.chekcers.styleChecker.ruleFactories;

import eu.qped.umr.model.XpathRule;

import java.util.Map;

public class XpathRuleFactory extends RuleFactory {

    protected XpathRuleFactory(){

    }

    public static XpathRule createXpathRule (String name, String message, String pmdClass, String description, Map<String, String> properties){
        return new XpathRule(name , message , pmdClass, description , properties);
    }

}
