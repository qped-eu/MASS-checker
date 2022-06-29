package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.ModifierType;
import java.util.*;

public interface AstFramework {

    AstCollection analyze(
            AstCollection collection,
            List<AstFacade> classes,
            Set<ModifierType> excludeByType,
            Set<String> excludeByName);

}
