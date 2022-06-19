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
import eu.qped.java.checkers.design.infos.ExpectedElement;

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
        List<ClassInfo> classInfos = designConfigurator.getClassInfos();
        List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        ClassMatcher classMatcher = new ClassMatcher();
        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        designFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));

        ModifierChecker<FieldDeclaration> fieldChecker = new ModifierChecker<>(CheckerUtils.FIELD_CHECKER);
        ModifierChecker<MethodDeclaration> methodChecker = new ModifierChecker<>(CheckerUtils.METHOD_CHECKER);
        InheritanceChecker inheritanceChecker = new InheritanceChecker(matchedDeclInfo);
        for(Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry : matchedDeclInfo.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getValue();
            ClassInfo classInfo = entry.getKey();

            List<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            List<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());
            expectedFieldKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));
            expectedMethodKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));

            ExpectedElement expectedDeclInfo = CheckerUtils.extractExpectedInfo(classInfo.getClassTypeName());
            designFeedbacks.addAll(classMatcher.checkClassMatch(classDecl, expectedDeclInfo));
            designFeedbacks.addAll(inheritanceChecker.checkInheritanceMatch(classDecl, getElementInfos(classInfo.getInheritsFrom())));
            designFeedbacks.addAll(fieldChecker.checkModifiers(classDecl, getElementInfos(expectedFieldKeywords)));
            designFeedbacks.addAll(methodChecker.checkModifiers(classDecl, getElementInfos(expectedMethodKeywords)));

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

    /**
     * Given a string, extract (access, non access, type, name) object out of it
     * @param expectedKeywords Strings to extract the keywords out of
     * @return list of all element infos regarding the keywords
     */
    private List<ExpectedElement> getElementInfos(List<String> expectedKeywords) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (String keywords: expectedKeywords) {
            infos.add(CheckerUtils.extractExpectedInfo(keywords));
        }
        return infos;
    }

    public void addSource(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    public List<DesignFeedback> getDesignFeedbacks() {
        return designFeedbacks;
    }

}
