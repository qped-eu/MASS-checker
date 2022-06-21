package eu.qped.java.checkers.coverage.feedback;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.enums.StatementType;
import eu.qped.java.checkers.coverage.feedback.wanted.ProviderWF;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageClass;

import java.util.*;


/**
 * Is a part of a tree-structure with the root in the class {@link Summary}.
 * In this node all information related to a class is stored.
 * @author Herfurth
 * @version 1.0
 */
public class ByClass {
    protected Map<String, ByMethod> methodByName = new HashMap<>();
    protected final Set<String> constructorKeys = new HashSet<>();
    protected CoverageClass aClass;

    ByClass(Node node) {
        insert(node);
    }

    void insert(Node node) {
        node.insert(this);
    }

    void analyze(ProviderWF provider) {
        if (Objects.isNull(aClass))
            methodByName.clear();

        constructorCovered();
        ByMethod byMethod;
        for (String key : methodByName.keySet()) {
            byMethod = methodByName.get(key);
            if (! byMethod.hasContent() || ! byMethod.hasCoverage()) {
                methodByName.remove(key, byMethod);
                continue;
            }
            byMethod.analyze(provider, aClass);
        }
    }

    void constructorCovered() {
        Map<String, ByMethod> isNot = new HashMap<>();

        ByMethod byMethod;
        SKIP:
        for (String key : constructorKeys) {
            byMethod = methodByName.get(key);
            byMethod.coverage = aClass;
            if (byMethod.hasContent()) {
                for (int i = byMethod.start() + 1; i <= byMethod.end(); i ++) {
                    StateOfCoverage state = aClass.byIndex(i);
                    if (state.equals(StateOfCoverage.FULL) || state.equals(StateOfCoverage.PARTLY)) {
                        continue SKIP;
                    }
                }
                methodByName.remove(key, byMethod);
                isNot.put(key, byMethod);
            } else {
                methodByName.remove(key, byMethod);
            }
        }

        if (isNot.size() == constructorKeys.size()) {
            methodByName = isNot;
        }

    }

    public List<ByMethod> byMethods() {
        List<ByMethod> m = new LinkedList<>(methodByName.values());
        Collections.sort(m);
        return m;
    }

    public CoverageCount branch() {
        return aClass.branch();
    }

    public CoverageCount line() {
        return aClass.line();
    }


    public String name() {
        return aClass.className();
    }

    public StateOfCoverage state() {
        return aClass.state();
    }

}
