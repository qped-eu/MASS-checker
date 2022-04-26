package eu.qped.java.checkers.design;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;

import java.lang.reflect.Method;
import java.util.*;

public class DesignChecker implements Checker {

    private String source;
    private DesignConfigurator designConfigurator;
    private CompilationUnit compilationUnit;
    private List<DesignFeedback> designFeedbacks;
    private final DesignFeedbackGenerator designFeedbackGenerator;

    public DesignChecker(DesignConfigurator designConfigurator) {
        designFeedbacks = new ArrayList<>();
        this.designConfigurator = designConfigurator;
        designFeedbackGenerator = DesignFeedbackGenerator.createDesignFeedbackGenerator();
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(designConfigurator == null) {
            designConfigurator = DesignConfigurator.createDefaultDesignConfigurator();
        }
        parseCompUnit();
        checkDesign();
    }

    private void parseCompUnit (){
        this.compilationUnit = StaticJavaParser.parse(this.source);
    }

    /**
     * Checks the design by comparing the expected modifiers and expected super classes with the actual given modifiers
     * and classes from source
     */
    private void checkDesign() {

        //TODO: Look at all the different classes
        for (ClassInfo classInfo: designConfigurator.getClassInfos()) {
            //check field modifiers
            //Create Copy so that we can delete matching elements in the list
            ArrayList<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            ModifierChecker<FieldDeclaration> fieldChecker = new ModifierChecker<>(compilationUnit, this, "field");
            if(designConfigurator.isModifierMaxRestrictive()) {
                fieldChecker.checkModifierMaxRestrictive();
            } else {
                fieldChecker.checkModifiers(expectedFieldKeywords);
            }


            //Check method modifiers
            ArrayList<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());
            ModifierChecker<MethodDeclaration> methodChecker = new ModifierChecker<>(compilationUnit, this, "method");
            if(designConfigurator.isModifierMaxRestrictive()) {
                methodChecker.checkModifierMaxRestrictive();
            } else {
                methodChecker.checkModifiers(expectedMethodKeywords);
            }

            //Check class type match and inheritance match
            String expectedClassTypeName = classInfo.getClassTypeName();
            ClassChecker classChecker = new ClassChecker(compilationUnit, this);
            ArrayList<String> expectedInheritsFrom = new ArrayList<>(classInfo.getInheritsFrom());

            classChecker.checkClassDeclaration(expectedClassTypeName, expectedInheritsFrom);
        }
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DesignFeedback> getDesignFeedbacks() {
        return designFeedbacks;
    }

    public void setDesignFeedbacks(ArrayList<DesignFeedback> designFeedbacks) {
        this.designFeedbacks = designFeedbacks;
    }
}
