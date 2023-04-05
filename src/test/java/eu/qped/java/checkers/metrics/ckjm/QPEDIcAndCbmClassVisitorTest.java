package eu.qped.java.checkers.metrics.ckjm;

import static org.junit.Assert.assertEquals;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import gr.spinellis.ckjm.utils.MethodCoupling;
import gr.spinellis.ckjm.utils.MethodInvokation;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;


public class QPEDIcAndCbmClassVisitorTest {

    private QPEDIcAndCbmClassVisitor visitor;

    @Before
    public void setUp() {
        visitor = new QPEDIcAndCbmClassVisitor(null);
    }

    @Ignore
    @Test
    public void testVisitJavaClass_body() throws IOException {
        ConstantUtf8[] constantUtf8s = new ConstantUtf8[1];
        constantUtf8s[0] = new ConstantUtf8("Hello, World!");

        ConstantPool constantPool = new ConstantPool(constantUtf8s);


        int[] interfaces = {1, 2, 3};

        Field[] fields = new Field[1];

        // Create an array of methods
        Method[] methods = new Method[1];
        methods[0] = new Method();

        Attribute[] attributes = new Attribute[1];
        attributes[0] = new Attribute((byte) 0, 10, 0, constantPool) {
            @Override
            public void accept(Visitor visitor) {

            }

            @Override
            public Attribute copy(ConstantPool constantPool) {
                return null;
            }
        };

        JavaClass jc = new JavaClass(0, 1, "MyClass", 52, 0, 1, constantPool, interfaces, fields, methods, attributes, (byte) 0);

        visitor.visitJavaClass_body(jc);


    }
}
