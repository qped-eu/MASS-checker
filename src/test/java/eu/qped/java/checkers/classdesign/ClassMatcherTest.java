package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import eu.qped.java.checkers.classdesign.exceptions.ClassNameException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.WRONG_CLASS_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ClassMatcherTest {

    @Test
    public void checkClassAmount() {
        List<ClassInfo> classInfos = new ArrayList<>();
        List<ClassOrInterfaceDeclaration> declarations = new ArrayList<>();
        ClassInfo info = new ClassInfo();
        ClassInfo info2 = new ClassInfo();
        classInfos.add(info);
        classInfos.add(info2);
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declarations.add(declaration);

        ClassFeedback fb = TestUtils.getFeedback("", "", ClassFeedbackType.MISSING_CLASSES);


        assertEquals(fb, ClassMatcher.checkClassAmount(classInfos,declarations));
    }

    @Test
    void matchClassNames() throws ClassNameException {
        List<ClassInfo> classInfos = new ArrayList<>();
        List<ClassOrInterfaceDeclaration> declarations = new ArrayList<>();
        ClassInfo info = new ClassInfo();
        ClassInfo info2 = new ClassInfo();
        classInfos.add(info);
        classInfos.add(info2);
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        ClassOrInterfaceDeclaration declaration2 = new ClassOrInterfaceDeclaration();
        declarations.add(declaration);
        declarations.add(declaration2);
        Map<ClassInfo, ClassOrInterfaceDeclaration> map = new HashMap<>();

        assertEquals(map,ClassMatcher.matchClassNames(declarations,classInfos));


    }

    @Test
    void generateClassNameFeedback() {
        List<ClassOrInterfaceDeclaration> classDecls = new ArrayList<>();
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        classDecls.add(declaration);
        ClassFeedback fb = new ClassFeedback("ClassNameError: Different name for **class empty** expected.\n" +
                "\n" );
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        collectedFeedback.add(fb);

        assertEquals(collectedFeedback, ClassMatcher.generateClassNameFeedback(classDecls));
    }

}