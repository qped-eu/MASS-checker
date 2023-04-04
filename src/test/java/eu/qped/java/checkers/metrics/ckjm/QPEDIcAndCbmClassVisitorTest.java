package eu.qped.java.checkers.metrics.ckjm;

import gr.spinellis.ckjm.IClassMetricsContainer;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;

public class QPEDIcAndCbmClassVisitorTest extends QPEDIcAndCbmClassVisitor {

    public QPEDIcAndCbmClassVisitorTest(IClassMetricsContainer classMap) {
        super(classMap);
    }

    @Override
    protected void visitJavaClass_body(JavaClass javaClass) {
        // implementation of the method
    }

    @Override
    public void visitConstantPool(ConstantPool constantPool) {
        // implementation of the method
    }

    @Override
    public void visitField(Field field) {
        // implementation of the method
    }

}
