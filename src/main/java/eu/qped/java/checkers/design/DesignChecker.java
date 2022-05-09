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

    public static final String DEFAULT_CLASS_TYPE = "class";
    public static final String ABSTRACT_CLASS_TYPE = "abstract class";
    public static final String INTERFACE_TYPE = "interface";

    public static final String FIELD_CHECKER = "field";
    public static final String METHOD_CHECKER = "method";

    private final List<CompilationUnit> compilationUnits;

    private final Map<ClassOrInterfaceDeclaration, ClassInfo> classDeclToInfoMap;

    private DesignConfigurator designConfigurator;
    private List<DesignFeedback> designFeedbacks;
    private final DesignFeedbackGenerator designFeedbackGenerator;



    public DesignChecker(DesignConfigurator designConfigurator) {
        designFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        classDeclToInfoMap = new HashMap<>();
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
        matchClassDeclAndInfo(compilationUnits, designConfigurator.getClassInfos());

        for(Map.Entry<ClassOrInterfaceDeclaration, ClassInfo> entry : classDeclToInfoMap.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            //check field modifiers
            //Create Copy so that we can delete matching elements in the list
            ArrayList<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            ModifierChecker<FieldDeclaration> fieldChecker = new ModifierChecker<>(classDecl, this, FIELD_CHECKER);
            if(designConfigurator.isModifierMaxRestrictive()) {
                fieldChecker.checkModifierMaxRestrictive();
            } else {
                fieldChecker.checkModifiers(expectedFieldKeywords);
            }


            //Check method modifiers
            ArrayList<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());
            ModifierChecker<MethodDeclaration> methodChecker = new ModifierChecker<>(classDecl, this, METHOD_CHECKER);
            if(designConfigurator.isModifierMaxRestrictive()) {
                methodChecker.checkModifierMaxRestrictive();
            } else {
                methodChecker.checkModifiers(expectedMethodKeywords);
            }

            //Check class type match and inheritance match
            ClassChecker classChecker = new ClassChecker(this);
            classChecker.checkSuperClassDeclaration(classDecl, classInfo);
        }
    }

    /**
     * Tries to match up given compilation units with given class infos
     * if there isn't an exact match between elements of those two, we generate feedback based on the name and type
     * of the expected class
     * @param compilationUnits compilation units to go through
     * @param classInfos expected class infos
     */
    public void matchClassDeclAndInfo(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
        List<ClassOrInterfaceDeclaration> classDeclarations = new ArrayList<>();
        //TODO: Deal with Inner Classes.
        for (CompilationUnit compUnit: compilationUnits) {
            List<ClassOrInterfaceDeclaration> foundClassDecls = compUnit.findAll(ClassOrInterfaceDeclaration.class);
            classDeclarations.add(foundClassDecls.get(0));
        }
        findExactClassDeclAndInfoMatch(classDeclarations, classInfos);
        generateFeedbackForMismatch(classDeclarations, classInfos);
    }

    /**
     * Tries to match up class declarations with their infos and generate feedback, if we cannot match them up exactly
     * @param classDeclarations compilation units to go through
     * @param classInfos expected class infos
     */
    private void findExactClassDeclAndInfoMatch(List<ClassOrInterfaceDeclaration> classDeclarations, List<ClassInfo> classInfos) {
        Iterator<ClassOrInterfaceDeclaration> declIterator = classDeclarations.iterator();

        //Find exact match between compilation unit and class infos here
        while(declIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = declIterator.next();

            //Find class info that belongs to this classDecl
            Iterator<ClassInfo> infoIterator = classInfos.iterator();

            while (infoIterator.hasNext()) {
                ClassInfo classInfo = infoIterator.next();
                String classTypeName = classInfo.getClassTypeName();

                if (classTypeName.isBlank() || !classTypeName.contains(":")) {
                    //Since classTypeName is blank or doesn't conform to standards
                    //we can't assign it to any class declaration
                    continue;
                }

                String[] classTypeNameSplit = classTypeName.split(":");
                String classType = classTypeNameSplit[0];
                String className = classTypeNameSplit[1];

                boolean foundTypeMatch = isTypeMatch(classDecl, classType);

                if (foundTypeMatch) {
                    if (classDecl.getNameAsString().equals(className)) {
                        //we can match the classInfo with the compUnit here since both fit
                        addClassDeclToMap(classDecl, classInfo);
                        declIterator.remove();
                        infoIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Generates feedback based on missing or wrong class type / name. we define missing or wrong as not being equal
     * to the expected class infos given by class infos
     * @param classDecls class declaration to generate feedback for
     * @param classInfos expected class infos
     */
    private void generateFeedbackForMismatch(List<ClassOrInterfaceDeclaration> classDecls, List<ClassInfo> classInfos) {
        for (ClassOrInterfaceDeclaration classDecl: classDecls) {
            boolean typeMatch = false;

            for (ClassInfo classInfo : classInfos) {
                String[] classTypeName = classInfo.getClassTypeName().split(":");
                String expectedClassType = classTypeName[0];
                String expectedClassName = classTypeName[1];

                //Check type first: If the type is right, we know the name has to be wrong
                typeMatch = isTypeMatch(classDecl, expectedClassType);

                //Since type matches, we know the name is wrong
                if(typeMatch) {
                    break;
                }
            }

            if(typeMatch) {
                addFeedback(classDecl.getNameAsString(), "", DesignFeedbackGenerator.WRONG_CLASS_NAME);
            } else {
                //Otherwise since the type doesn't match up, we give feedback based on wrong class type
                addFeedback(classDecl.getNameAsString(), "", DesignFeedbackGenerator.WRONG_CLASS_TYPE);
            }
        }
    }




    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isTypeMatch(ClassOrInterfaceDeclaration classDecl, String classType) {
        boolean foundTypeMatch = false;
        switch (classType) {
            case INTERFACE_TYPE:
                foundTypeMatch = classDecl.isInterface();
                break;
            case ABSTRACT_CLASS_TYPE:
                foundTypeMatch = classDecl.isAbstract();
                break;
            case DEFAULT_CLASS_TYPE:
                foundTypeMatch = !classDecl.isInterface() && !classDecl.isAbstract();
                break;
        }
        return foundTypeMatch;
    }


    public Map<ClassOrInterfaceDeclaration, ClassInfo> getClassDeclToInfoMap() {
        return classDeclToInfoMap;
    }

    public void addClassDeclToMap(ClassOrInterfaceDeclaration classDecl, ClassInfo classInfo) {
        getClassDeclToInfoMap().put(classDecl, classInfo);
    }

    /**
     * Adds generated Feedback to the complete list
     * @param elementName identifies the violating element of the class
     * @param violationType violation identifier for the map in designFeedbackGenerator
     */
    public void addFeedback(String className, String elementName, String violationType) {
        DesignFeedback designFeedback = designFeedbackGenerator.generateFeedback(className, elementName, violationType);
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

}
