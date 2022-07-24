package eu.qped.java.checkers.classdesign;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.config.KeywordConfig;
import eu.qped.java.checkers.classdesign.enums.ClassMemberType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Main Checker that delegates work to each individual element checker.
 *
 * @author Paul Engelmann
 */
public class ClassChecker implements Checker {

    private String targetPath;
    private final JavaParser javaParser;

    private final List<CompilationUnit> compilationUnits;

    private ClassConfigurator classConfigurator;
    private final List<ClassFeedback> classFeedbacks;

    public ClassChecker(ClassConfigurator classConfigurator) {
        targetPath = "";
        javaParser = new JavaParser();
        classFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        this.classConfigurator = classConfigurator;
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(classConfigurator == null) {
            classConfigurator = ClassConfigurator.createDefaultClassConfigurator();
        }

        if(!targetPath.isBlank()) {
            parseCompUnitsFromFiles();
        }
        checkClasses();
    }

    /**
     * Check the parsed compilation units and compare them to the expected elements in class infos to generate
     * feedback. This method delegates each task to each class and collects all feedback
     */
    private void checkClasses() {
        List<ClassInfo> classInfos = classConfigurator.getClassInfos();
        List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        checkClassAmount(classInfos, classDecls);

        ClassMatcher classMatcher = new ClassMatcher();
        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        classFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));

        for(Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry : matchedDeclInfo.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getValue();
            ClassInfo classInfo = entry.getKey();

            List<KeywordConfig> fieldKeywordConfigs = new ArrayList<>(classInfo.getFieldKeywordConfigs());
            List<KeywordConfig> methodKeywordConfigs = new ArrayList<>(classInfo.getMethodKeywordConfigs());
            List<KeywordConfig> inheritsFromConfigs = new ArrayList<>(classInfo.getInheritsFromConfigs());

            ClassMemberChecker<FieldDeclaration> fieldChecker = new ClassMemberChecker<>(ClassMemberType.FIELD,
                    classInfo.getCustomFieldFeedback());
            ClassMemberChecker<MethodDeclaration> methodChecker = new ClassMemberChecker<>(ClassMemberType.METHOD,
                    classInfo.getCustomMethodFeedback());

            InheritanceChecker inheritanceChecker = new InheritanceChecker(matchedDeclInfo, classInfo.getCustomInheritanceFeedback());

            boolean matchExactFieldAmount = classInfo.isMatchExactFieldAmount();
            boolean matchExactMethodAmount = classInfo.isMatchExactMethodAmount();

            classFeedbacks.addAll(classMatcher.checkClassMatch(classDecl, CheckerUtils.extractExpectedInfo(classInfo.getClassKeywordConfig()), classInfo.getCustomClassFeedback()));
            classFeedbacks.addAll(inheritanceChecker.checkInheritanceMatch(classDecl, getExpectedInfos(inheritsFromConfigs)));
            classFeedbacks.addAll(fieldChecker.checkModifiers(classDecl, getExpectedInfos(fieldKeywordConfigs), matchExactFieldAmount));
            classFeedbacks.addAll(methodChecker.checkModifiers(classDecl, getExpectedInfos(methodKeywordConfigs), matchExactMethodAmount));

        }
    }

    /**
     * Parse through files if they were used for answering the task.
     * @throws FileNotFoundException if file can not be found
     * @throws IllegalArgumentException if file can not be parsed
     */
    private void parseCompUnitsFromFiles() throws FileNotFoundException, IllegalArgumentException {
        File javaOrDirFile = new File(targetPath);

        if(javaOrDirFile.exists()) {
            if(javaOrDirFile.isDirectory()) {
                for (File javaFile : Objects.requireNonNull(javaOrDirFile.listFiles())) {
                    CompilationUnit compUnit = parseFile(javaFile);
                    compilationUnits.add(compUnit);
                }
            } else {
                if(javaOrDirFile.isFile()) {
                    CompilationUnit compUnit = parseFile(javaOrDirFile);
                    compilationUnits.add(compUnit);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private CompilationUnit parseFile(File file) throws FileNotFoundException {
        ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
        if(parseResult.getResult().isPresent()) {
            return parseResult.getResult().get();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Make sure that the amount of provided classes matches up with the amount of expected classes
     * @param classInfos expected class information
     * @param classDecls provided classes
     */
    private void checkClassAmount(List<ClassInfo> classInfos, List<ClassOrInterfaceDeclaration> classDecls) {
        if(classInfos.size() > classDecls.size()) {
            classFeedbacks.add(ClassFeedbackGenerator.generateFeedback("", "", ClassFeedbackType.MISSING_CLASSES, ""));
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


    private List<ExpectedElement> getExpectedInfos(List<KeywordConfig> keywordConfigs) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (KeywordConfig keywords: keywordConfigs) {
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

    public List<ClassFeedback> getClassFeedbacks() {
        return classFeedbacks;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

}
