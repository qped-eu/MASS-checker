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
import eu.qped.java.checkers.classdesign.exceptions.ClassNameException;
import eu.qped.java.checkers.classdesign.exceptions.NoModifierException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
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

    /**
     * check if the object is null or not( if the object have any Elements within)
     * @param qfObject Qfobject which contains settings, username , ...
     * @throws Exception
     */
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
    private void checkClasses() throws ClassNameException, NoModifierException {
        final List<ClassInfo> classInfos = classConfigurator.getClassInfos();
        final List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        ClassMatcher classMatcher = new ClassMatcher();
        final ClassFeedback amountFB = classMatcher.checkClassAmount(classInfos, classDecls);
        if(amountFB != null) {
            classFeedbacks.add(amountFB);
        }

        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        classFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));
        ClassOrInterfaceDeclaration classDecl; // Objekt deklarieren und initalsiern
        ClassInfo classInfo;
        boolean matchExactFieldAmount;
        boolean matchExactMethodAmount;
        for(Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry : matchedDeclInfo.entrySet()) {
            classDecl = entry.getValue(); // Obejkt nach jedem Aufruf überschreiben
            classInfo = entry.getKey();

            List<KeywordConfig> fieldKeywordConfigs = new ArrayList<>(classInfo.getFieldKeywordConfigs());
            List<KeywordConfig> methodKeywordConfigs = new ArrayList<>(classInfo.getMethodKeywordConfigs());
            List<KeywordConfig> inheritsFromConfigs = new ArrayList<>(classInfo.getInheritsFromConfigs());

            ClassMemberChecker<FieldDeclaration> fieldChecker = new ClassMemberChecker<>(ClassMemberType.FIELD,
                    classInfo.getCustomFieldFeedback());
            ClassMemberChecker<MethodDeclaration> methodChecker = new ClassMemberChecker<>(ClassMemberType.METHOD,
                    classInfo.getCustomMethodFeedback());

            InheritanceChecker inheritanceChecker = new InheritanceChecker(matchedDeclInfo, classInfo.getCustomInheritanceFeedback());

            matchExactFieldAmount = classInfo.isMatchExactFieldAmount(); // oben deklariert
            matchExactMethodAmount = classInfo.isMatchExactMethodAmount();

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
        final File javaOrDirFile = new File(targetPath);

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

    /**
     * read the file and check the result if the file was java, otherwise throws an exception
     * @param file java file ends with .class or .java
     * @return the result of the input java code
     * @throws FileNotFoundException
     */
    private CompilationUnit parseFile(File file) throws FileNotFoundException {
        ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
        if(parseResult.getResult().isPresent()) {
            return parseResult.getResult().get();
        } else {
            throw new IllegalArgumentException();
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
     * Extract all possible modifiers, type and name from the field configuration provided by json and add it to a list of (expected element)
     * @param keywordConfigs field keyword modifiers from json
     * @return a list with all possible modifiers
     * @throws NoModifierException
     */
    private List<ExpectedElement> getExpectedInfos(List<KeywordConfig> keywordConfigs) throws NoModifierException {
        List<ExpectedElement> infos = new ArrayList<>();
        for (KeywordConfig keywords: keywordConfigs) {
            infos.add(CheckerUtils.extractExpectedInfo(keywords));
        }
        return infos;
    }


    /**
     * Chose the source of the code
     * @param source the source of the file we upload, whether it is java or anything else
     */
    public void addSource(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    /**
     * @param source the source of the file we upload, whether it is java or anything else
     * @return the parsing code as CompilationUnit object
     */
    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    /**
     * @return a list of all feedbacks of the code
     */
    public List<ClassFeedback> getClassFeedbacks() {
        return classFeedbacks;
    }

    /**
     * @return where the user save the feedback file
     */
    public String getTargetPath() {
        return targetPath;
    }

    /**
     * @param targetPath the path where the user want to save the feedback file
     */
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

}
