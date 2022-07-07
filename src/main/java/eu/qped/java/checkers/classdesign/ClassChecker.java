package eu.qped.java.checkers.classdesign;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.classdesign.config.InheritsFromConfig;
import eu.qped.java.checkers.classdesign.config.KeywordConfig;
import eu.qped.java.checkers.classdesign.config.MethodKeywordConfig;
import eu.qped.java.checkers.classdesign.enums.ClassMemberType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.infos.*;

import java.util.*;

/**
 * Main Checker that delegates work to each class element checker.
 *
 * @author Paul Engelmann
 */
public class ClassChecker implements Checker {

    private final List<CompilationUnit> compilationUnits;

    private ClassConfigurator classConfigurator;
    private final List<ClassFeedback> classFeedbacks;

    public ClassChecker(ClassConfigurator classConfigurator) {
        classFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        this.classConfigurator = classConfigurator;
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(classConfigurator == null) {
            classConfigurator = ClassConfigurator.createDefaultClassConfigurator();
        }
        checkClass();
    }

    /**
     * Check the parsed compilation units and compare them to the expected elements in class infos to generate
     * feedback. This method delegates each task to each class and collects all feedback
     */
    private void checkClass() {
        List<ClassInfo> classInfos = classConfigurator.getClassInfos();
        List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        ClassMatcher classMatcher = new ClassMatcher();
        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        classFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));

        for(Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry : matchedDeclInfo.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getValue();
            ClassInfo classInfo = entry.getKey();

            List<KeywordConfig> fieldKeywordConfigs = new ArrayList<>(classInfo.getFieldKeywordConfigs());
            List<KeywordConfig> methodKeywordConfigs = new ArrayList<>(classInfo.getMethodKeywordConfigs());

            ClassMemberChecker<FieldDeclaration> fieldChecker = new ClassMemberChecker<>(ClassMemberType.FIELD,
                    classInfo.getCustomFieldFeedback());
            ClassMemberChecker<MethodDeclaration> methodChecker = new ClassMemberChecker<>(ClassMemberType.METHOD,
                    classInfo.getCustomMethodFeedback());

            InheritanceChecker inheritanceChecker = new InheritanceChecker(matchedDeclInfo, classInfo.getCustomInheritanceFeedback());

            classFeedbacks.addAll(classMatcher.checkClassMatch(classDecl, CheckerUtils.extractExpectedClassInfo(classInfo.getClassKeywordConfig()), classInfo.getCustomClassFeedback()));
            classFeedbacks.addAll(inheritanceChecker.checkInheritanceMatch(classDecl, getInheritsFromInfos(classInfo.getInheritsFrom())));
            classFeedbacks.addAll(fieldChecker.checkModifiers(classDecl, getFieldInfos(fieldKeywordConfigs)));
            classFeedbacks.addAll(methodChecker.checkModifiers(classDecl, getMethodInfos(methodKeywordConfigs)));

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

    private List<ExpectedElement> getInheritsFromInfos(List<InheritsFromConfig> keywordConfigs) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (InheritsFromConfig keywords: keywordConfigs) {
            infos.add(CheckerUtils.extractExpectedInheritsFromInfo(keywords));
        }
        return infos;
    }

    private List<ExpectedElement> getFieldInfos(List<KeywordConfig> fieldKeywordConfigs) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (KeywordConfig keywords: fieldKeywordConfigs) {
            FieldKeywordConfig fieldKeywordConfig = (FieldKeywordConfig) keywords;
            infos.add(CheckerUtils.extractExpectedFieldInfo(fieldKeywordConfig));
        }
        return infos;
    }

    private List<ExpectedElement> getMethodInfos(List<KeywordConfig> methodKeywordConfigs) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (KeywordConfig keywords: methodKeywordConfigs) {
            MethodKeywordConfig methodKeywordConfig = (MethodKeywordConfig) keywords;
            infos.add(CheckerUtils.extractExpectedMethodInfo(methodKeywordConfig));
        }
        return infos;
    }

    public void addSource(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    public List<ClassFeedback> getClassFeedbacks() {
        return classFeedbacks;
    }

}
