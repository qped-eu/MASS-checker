package eu.qped.java.checkers.metrics.ckjm;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static jdk.internal.org.objectweb.asm.Opcodes.V1_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import gr.spinellis.ckjm.CkjmOutputHandler;
import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.IClassMetricsContainer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import org.apache.bcel.classfile.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QPEDIcAndCbmClassVisitorTest {

    private QPEDIcAndCbmClassVisitor classVisitor;
    private IClassMetricsContainer classMap;

    @BeforeEach
    public void setUp() {
        classMap = new IClassMetricsContainer() {
            @Override
            public ClassMetrics getMetrics(String s) {
                return null;
            }

            @Override
            public void printMetrics(CkjmOutputHandler ckjmOutputHandler) {

            }
        };
        classVisitor = new QPEDIcAndCbmClassVisitor(classMap);
    }

    @Test
    public void testVisitJavaClass_body_initializesVariables() {
        assertEquals(0, classVisitor.getCase1());
        assertEquals(0, classVisitor.getCase2());
        assertEquals(0, classVisitor.getCase3());
    }

    @Test
    public void testVisitJavaClass_body_setsCurrentClass() throws InstantiationException, IOException {
        JavaClass javaClass = new JavaClass(0, 0, "", 0, 0, 0, null, null, null, null, null);
        classVisitor.visitJavaClass_body(javaClass);
        assertEquals(javaClass, classVisitor.getCurrentClass());

    }

    @Test
    public void testVisitJavaClass_body_initializesParentMethods() {
        classVisitor.visitJavaClass_body(mock(JavaClass.class));
        assertNotNull(classVisitor.getParentMethods());
        assertTrue(classVisitor.getParentMethods().isEmpty());
    }

    @Test
    public void testVisitJavaClass_body_initializesSets() {
        classVisitor.visitJavaClass_body(mock(JavaClass.class));
        assertNotNull(classVisitor.getInvFormParents());
        assertTrue(classVisitor.getInvFormParents().isEmpty());
        assertNotNull(classVisitor.getInvFromCClass());
        assertTrue(classVisitor.getInvFromCClass().isEmpty());
        assertNotNull(classVisitor.getParentsReaders());
        assertTrue(classVisitor.getParentsReaders().isEmpty());
        assertNotNull(classVisitor.getcClassSetters());
        assertTrue(classVisitor.getcClassSetters().isEmpty());
        assertNotNull(classVisitor.getMethodCouplings());
        assertTrue(classVisitor.getMethodCouplings().isEmpty());
    }

    @Test
    void testVisitJavaClass_body_populatesParentMethods() throws ClassNotFoundException {
        // Create a mock JavaClass to represent the current class being visited
        JavaClass currentClass = mock(JavaClass.class);
        when(currentClass.getSuperClasses()).thenReturn(new JavaClass[]{mock(JavaClass.class), mock(JavaClass.class)});

        // Create mock methods for the current class and its parents
        Method[] currentMethods = {mock(Method.class), mock(Method.class)};
        when(currentClass.getMethods()).thenReturn(currentMethods);
        Method[] parentMethods1 = {mock(Method.class), mock(Method.class)};
        Method[] parentMethods2 = {mock(Method.class)};

        // Create a QPEDIcAndCbmClassVisitor instance
        IClassMetricsContainer classMap = mock(IClassMetricsContainer.class);
        QPEDIcAndCbmClassVisitor visitor = new QPEDIcAndCbmClassVisitor(classMap);

        // Call the visitJavaClass_body method
        visitor.visitJavaClass_body(currentClass);

        // Verify that the parentMethods list has been correctly populated
        assertEquals(2, visitor.getParentMethods().size());
        assertArrayEquals(parentMethods1, visitor.getParentMethods().get(0));
        assertArrayEquals(parentMethods2, visitor.getParentMethods().get(1));
    }
}

