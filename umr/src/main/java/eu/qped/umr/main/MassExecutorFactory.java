package eu.qped.umr.main;

import eu.qped.umr.checkers.semanticChecker.SemanticChecker;
import eu.qped.umr.checkers.styleChecker.StyleChecker;
import eu.qped.umr.checkers.syntaxChecker.SyntaxErrorChecker;

public class MassExecutorFactory {

    public static MassExecutor createExecutorWithDefaults(MainSettingsConfigurator mainSettingsConfigurator) {
        return new MassExecutor(null, null, null, mainSettingsConfigurator);
    }

    public static MassExecutor createMassExecutor(final StyleChecker styleChecker, final SemanticChecker semanticChecker,
                                                  final SyntaxErrorChecker syntaxErrorChecker, final MainSettingsConfigurator mainSettingsConfigurator) {
        return new MassExecutor(styleChecker, semanticChecker, syntaxErrorChecker, mainSettingsConfigurator);
    }
}