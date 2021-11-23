package eu.qped.umr.checkers.autoChecker;

import eu.qped.umr.checkers.MassChecker;
import eu.qped.umr.main.MainSettingsConfigurator;
import eu.qped.umr.main.MassExecutor;
import eu.qped.umr.main.MassExecutorFactory;
import eu.qped.umr.model.StyleViolation;
import eu.qped.umr.model.SyntaxError;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class AutoChecker implements MassChecker {

    private Map<String , Integer> styleMap = new HashMap<>();

    @Override
    public void check() {
        Map<String, String> mainSettings = new HashMap<>();

        mainSettings.put("semanticNeeded", "false");
        mainSettings.put("syntaxLevel", "1");
        mainSettings.put("preferredLanguage", "en");
        mainSettings.put("styleNeeded", "true");


        MainSettingsConfigurator mainSettingsConfiguratorConf = new MainSettingsConfigurator(mainSettings);


        Map<String, String> styleConf = new HashMap<>();

        //styleConf.put("mainLevel" , "1");

        styleConf.put("namesLevel", "3");
        styleConf.put("compLevel", "3");
        styleConf.put("basisLevel", "3");
        styleConf.put("maxClassLength", "250");
        styleConf.put("maxMethodLength", "-1");
        styleConf.put("varNamesRegEx", "-1");
        styleConf.put("methodNamesRegEx", "-1");
        styleConf.put("classNameRegEx", "-1");
        styleConf.put("maxCycloComplexity", "-1");
        styleConf.put("maxFieldsCount", "-1");


        Map<String, String> semanticConf = new HashMap<>();

        semanticConf.put("methodName", "rec");
        semanticConf.put("recursionAllowed", "true");
        semanticConf.put("whereLoop", "-1");
        semanticConf.put("forLoop", "1");
        semanticConf.put("forEachLoop", "-1");
        semanticConf.put("ifElseStmt", "-1");
        semanticConf.put("doWhileLoop", "-1");
        semanticConf.put("returnType", "undefined");

        DataProvider dp = DataProvider.createDataProvider();
        dp.provide();
        List<String> data = dp.getData();
        List<StyleViolation> styleCheckerRes = new ArrayList<>();
        List<SyntaxError> syntaxCheckerRes = new ArrayList<>();

        for (String answer : data){
            MassExecutor massExecutor = MassExecutorFactory.createExecutorWithDefaults(null );
            massExecutor.execute();
            styleCheckerRes.addAll(massExecutor.getViolations());
            syntaxCheckerRes.addAll(massExecutor.getSyntaxErrors());
        }
//        Map<String,Integer> SyntaxMap = syntaxCheckerRes.stream().map(SyntaxError::getErrorCode).collect(groupingBy(Function.identity(), summingInt(e -> 1)));

        styleMap = styleCheckerRes.stream().map(StyleViolation::getRule).collect(groupingBy(Function.identity(), summingInt(e -> 1)));
    }

    public static void main(String[] args) {
        AutoChecker autoChecker = new AutoChecker();
        autoChecker.check();
        TableBuilder tb = TableBuilder.createTableBuilder(autoChecker.getStyleMap());
        tb.build();
        tb.closeDocument();

        int type = 0;

        int e = 0;
        int as =1 ;

    }

    public Map<String, Integer> getStyleMap() {
        return styleMap;
    }
}
