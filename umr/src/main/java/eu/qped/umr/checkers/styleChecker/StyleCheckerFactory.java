package eu.qped.umr.checkers.styleChecker;

import eu.qped.umr.checkers.styleChecker.configs.StyleConfigurator;

public class StyleCheckerFactory {

    public static StyleChecker createStyleChecker(final StyleConfigurator styleConfigurator) {
        return new StyleChecker(styleConfigurator);
    }

    public static StyleChecker createDefaultStyleChecker() {
        return new StyleChecker(null);
    }
}