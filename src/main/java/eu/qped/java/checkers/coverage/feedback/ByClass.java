package eu.qped.java.checkers.coverage.feedback;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.enums.StatementType;
import eu.qped.java.checkers.coverage.feedback.wanted.ProviderWF;
import eu.qped.java.checkers.coverage.framework.ast.AstMethod;
import eu.qped.java.checkers.coverage.framework.coverage.Coverage;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageClass;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageMethod;

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

        for (Iterator<String> it = methodByName.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            byMethod = methodByName.get(key);
            if (! byMethod.hasContent() || ! byMethod.hasCoverage()) {
                it.remove();
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
                if (byMethod.coverage.state().equals(StateOfCoverage.NOT)) {
                    methodByName.remove(key, byMethod);
                    isNot.put(key, byMethod);
                }
            } else {
                methodByName.remove(key, byMethod);
            }
        }

        if (isNot.size() == constructorKeys.size() && ! isNot.isEmpty()) {
            methodByName = isNot;
        } else if (constructorKeys.isEmpty() && aClass.state().equals(StateOfCoverage.NOT))  {
            methodByName.clear();
            methodByName.put("default", new ByMethod(new Node(aClass) {
                @Override
                String keyByClass() {
                    return null;
                }

                @Override
                String className() {
                    return null;
                }

                @Override
                String keyByMethod() {
                    return null;
                }

                @Override
                String methodName() {
                    return null;
                }

                @Override
                void insert(ByMethod byMethod) {
                    byMethod.coverage = aClass;
                    byMethod.content = new AstMethod(
                            StatementType.CONSTRUCTOR,
                            aClass.className(),
                            aClass.className(),
                            0,
                            0,
                            List.of("public "+ aClass.className()+"(){",
                                    "   DEFAULT CONSTRUTOR",
                                    "}")
                    );
                }
            }));
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
