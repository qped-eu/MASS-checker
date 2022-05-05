package eu.qped.java.checkers.design;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;

import java.util.*;

public class DesignChecker implements Checker {

    private final List<CompilationUnit> compilationUnits;
    private final Map<CompilationUnit, ClassInfo> compUnitToClassInfoMap;
    private DesignConfigurator designConfigurator;
    private List<DesignFeedback> designFeedbacks;
    private final DesignFeedbackGenerator designFeedbackGenerator;

    public DesignChecker(DesignConfigurator designConfigurator) {
        designFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        compUnitToClassInfoMap = new HashMap<>();
        this.designConfigurator = designConfigurator;
        designFeedbackGenerator = DesignFeedbackGenerator.createDesignFeedbackGenerator();
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(designConfigurator == null) {
            designConfigurator = DesignConfigurator.createDefaultDesignConfigurator();
        }
        checkDesign();
    }

    /**
     * Checks the design by comparing the expected modifiers and expected super classes with the actual given modifiers
     * and classes from source
     */
    private void checkDesign() {
        //TODO: Figure out what each class is and how they are in relation to each other
        //Check class type match and inheritance match
        ClassChecker classChecker = new ClassChecker(new ArrayList<>(compilationUnits),
                new ArrayList<>(designConfigurator.getClassInfos()),
                this);

        classChecker.checkClassDeclaration();

        for(Map.Entry<CompilationUnit, ClassInfo> entry :compUnitToClassInfoMap.entrySet()) {
            CompilationUnit compUnit = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            //check field modifiers
            //Create Copy so that we can delete matching elements in the list
            ArrayList<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            ModifierChecker<FieldDeclaration> fieldChecker = new ModifierChecker<>(compUnit, this, "field");
            if(designConfigurator.isModifierMaxRestrictive()) {
                fieldChecker.checkModifierMaxRestrictive();
            } else {
                fieldChecker.checkModifiers(expectedFieldKeywords);
            }


            //Check method modifiers
            ArrayList<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());
            ModifierChecker<MethodDeclaration> methodChecker = new ModifierChecker<>(compUnit, this, "method");
            if(designConfigurator.isModifierMaxRestrictive()) {
                methodChecker.checkModifierMaxRestrictive();
            } else {
                methodChecker.checkModifiers(expectedMethodKeywords);
            }
        }


    }

    public Map<CompilationUnit, ClassInfo> getCompUnitToClassInfoMap() {
        return compUnitToClassInfoMap;
    }

    public void addCompUnitToMap(CompilationUnit compilationUnit, ClassInfo classInfo) {
        getCompUnitToClassInfoMap().put(compilationUnit, classInfo);
    }

    /**
     * Adds generated Feedback to the complete list
     * @param name identifies the violating element of the class
     * @param violationType violation identifier for the map in designFeedbackGenerator
     */
    public void addFeedback(String violationType, String name) {
        DesignFeedback designFeedback = designFeedbackGenerator.generateFeedback(violationType, name);
        designFeedbacks.add(designFeedback);
    }



    public void addSourceCode(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    public List<DesignFeedback> getDesignFeedbacks() {
        return designFeedbacks;
    }

    public void setDesignFeedbacks(ArrayList<DesignFeedback> designFeedbacks) {
        this.designFeedbacks = designFeedbacks;
    }
}
