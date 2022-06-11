package eu.qped.java.checkers.design;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.infos.ClassInfo;

import java.util.*;

public class DesignChecker implements Checker {

    private final List<CompilationUnit> compilationUnits;

    private DesignConfigurator designConfigurator;
    private final List<DesignFeedback> designFeedbacks;

    public DesignChecker(DesignConfigurator designConfigurator) {
        designFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        this.designConfigurator = designConfigurator;
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(designConfigurator == null) {
            designConfigurator = DesignConfigurator.createDefaultDesignConfigurator();
        }
        checkDesign();
    }

    /**
     * Check the parsed compilation units and compare them to the expected elements in class infos to generate
     * feedback. This method delegates each task to each class and collects all feedback
     */
    private void checkDesign() {
        ClassMatcher classMatcher = new ClassMatcher();
        ModifierChecker<FieldDeclaration> fieldChecker = new ModifierChecker<>(CheckerUtils.FIELD_CHECKER);
        ModifierChecker<MethodDeclaration> methodChecker = new ModifierChecker<>(CheckerUtils.METHOD_CHECKER);
        InheritanceChecker inheritanceChecker = new InheritanceChecker();

        List<ClassInfo> classInfos = designConfigurator.getClassInfos();
        List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        Map<ClassOrInterfaceDeclaration, ClassInfo> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        designFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));

        for(Map.Entry<ClassOrInterfaceDeclaration, ClassInfo> entry : matchedDeclInfo.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            List<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            List<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());

            expectedFieldKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));
            expectedMethodKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));

            designFeedbacks.addAll(classMatcher.checkClassMatch(classDecl, classInfo.getClassTypeName()));
            designFeedbacks.addAll(fieldChecker.checkModifiers(classDecl, expectedFieldKeywords));
            designFeedbacks.addAll(methodChecker.checkModifiers(classDecl, expectedMethodKeywords));
            designFeedbacks.addAll(inheritanceChecker.checkInheritanceMatch(classDecl, classInfo.getInheritsFrom()));
        }
    }

    /**
     * Find all class declarations in all compilation units such that we can analyse them later
     * @param compilationUnits compilation units from source code given
     * @return list of all found class declarations
     */
    private List<ClassOrInterfaceDeclaration> getAllClassDeclarations(List<CompilationUnit> compilationUnits) {
        List<ClassOrInterfaceDeclaration> classDeclarations = new ArrayList<>();
        for (CompilationUnit compUnit: compilationUnits) {
            List<ClassOrInterfaceDeclaration> foundClassDecls = compUnit.findAll(ClassOrInterfaceDeclaration.class);
            classDeclarations.addAll(foundClassDecls);
        }
        return classDeclarations;
    }

    public void addCompilationUnit(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    public List<DesignFeedback> getDesignFeedbacks() {
        return designFeedbacks;
    }

}
