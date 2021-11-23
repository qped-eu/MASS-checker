package eu.qped.umr.chekcers.styleChecker;

import eu.qped.umr.chekcers.styleChecker.configs.StyleConfigurator;

public class StyleCheckerFactory {

    public static StyleChecker createStyleChecker(final StyleConfigurator styleConfigurator) {
        return new StyleChecker(styleConfigurator);
    }

    public static StyleChecker createDefaultStyleChecker() {
        return new StyleChecker(null);
    }
}