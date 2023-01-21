package eu.qped.java.checkers.metrics.ckjm;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import gr.spinellis.ckjm.AbstractClassVisitor;
import gr.spinellis.ckjm.IClassMetricsContainer;
import gr.spinellis.ckjm.utils.FieldAccess;
import gr.spinellis.ckjm.utils.LoggerHelper;
import gr.spinellis.ckjm.utils.MethodCoupling;
import gr.spinellis.ckjm.utils.MethodInvokation;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.*;

/**
 * Custom visitor class for the metrics:
 * IC {@link Metric#IC} and
 * CBM {@link Metric#CBM}.
 *
 * @author marian (from CKJM-extended tool)
 * @author Jannik Seus (edited)
 */
public class QPEDIcAndCbmClassVisitor extends AbstractClassVisitor {

    /**The array of methods*/
    private Method[] methods;
    /**The current class*/
    private JavaClass currentClass;
    /**The parent pool*/
    private ConstantPoolGen parentPool;
    /**The parent methods*/
    private List<Method[]> parentMethods;
    /**Invocations from parents*/
    private Set<MethodInvokation> invFormParents;
    /**invocations from current class*/
    private Set<MethodInvokation> invFromCClass;
    /**parents readers*/
    private Set<FieldAccess> parentsReaders;
    /**current class setters*/
    private Set<FieldAccess> cClassSetters;
    /**method couplings*/
    private Set<MethodCoupling> methodCouplings;
    /**parents*/
    private JavaClass[] parents;
    /**parent*/
    private JavaClass parent;

    /**
     * determines how many inherited methods use a field, that is defined
     * in a new/redefined method.
     */
    private int case1;

    /**
     * determines how many inherited methods call a redefined method
     * and use the return value of the redefined method.
     */
    private int case2;

    /**
     * determines how many inherited methods are called by a redefined method
     * and use a parameter that is defined in the redefined method.
     */
    private int case3;

    /**
     * Constrcutor
     * @param classMap is passed to the constructor of the parent class
     */
    public QPEDIcAndCbmClassVisitor(final IClassMetricsContainer classMap) {
        super(classMap);
    }

    @Override
    protected void visitJavaClass_body(final JavaClass javaClass) {
        case1 = case2 = case3 = 0;
        currentClass = javaClass;
        try {
            parents = javaClass.getSuperClasses();
            parentMethods = new ArrayList<>();
            methods = javaClass.getMethods();
            invFormParents = new TreeSet<>();
            invFromCClass = new TreeSet<>();
            parentsReaders = new TreeSet<>();
            cClassSetters = new TreeSet<>();
            methodCouplings = new TreeSet<>();

            for (final JavaClass j : parents) {
                parentPool = new ConstantPoolGen(j.getConstantPool());
                parent = j;
                parentMethods.add(j.getMethods());
                for (final Method m : j.getMethods()) {
                    m.accept(this);
                }
            }

            for (final Method m : methods) {
                if (hasBeenDefinedInParentToo(m)) {
                    investigateMethod(m);
                }
                investigateMethodAndLookForSetters(m);
            }

            countCase1();
            countCase2();
            countCase3();
            saveResults();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);

        }
    }

    /**
     * Used to visit methods of parents of investigated class.
     */
    @Override
    public void visitMethod(final Method mehtod) {
        final MethodGen methodGen = new MethodGen(mehtod, getParentClassName(), parentPool);
        if (!methodGen.isAbstract() && !methodGen.isNative()) {
            for (InstructionHandle ih = methodGen.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
                final Instruction instruction = ih.getInstruction();
                if (instructionNotVisited(instruction)) {

                    instruction.accept(new EmptyVisitor() {

                        @Override
                        public void visitInvokeInstruction(final InvokeInstruction invokeInstruction) {
                            String methodName;
                            String className;
                            final Type[] args = invokeInstruction.getArgumentTypes(parentPool);
                            methodName = invokeInstruction.getMethodName(parentPool);
                            className = invokeInstruction.getClassName(parentPool);

                            final MethodInvokation methodInvokation = new MethodInvokation(className, methodName, args, getParentClassName(), mehtod.getName(), mehtod.getArgumentTypes());
                            invFormParents.add(methodInvokation);

                        }

                        @Override
                        public void visitFieldInstruction(final FieldInstruction fieldInstruction) {
                            if (isGetInstruction(fieldInstruction)) {
                                final FieldAccess fieldAccess = new FieldAccess(fieldInstruction.getFieldName(parentPool), mehtod, parent);
                                parentsReaders.add(fieldAccess);
                            }
                        }

                        private boolean isGetInstruction(final FieldInstruction fieldInstruction) {
                            final String instr = fieldInstruction.toString(parentPool.getConstantPool());
                            return instr.startsWith("get");
                        }

                    });
                }
            }
        }
    }

    /**
     * Determines whether a given methodInvocation is a call of a redefined method.
     *
     * @param methodInvocation the given method invocation to be checked
     * @return true when methodInvocation is an invocation of a method
     * that has been redefined in investigated class.
     */
    private boolean callsRedefinedMethod(final MethodInvokation methodInvocation) {
        boolean result = false;
        for (final Method method : methods) {
            if (isInvocationOfTheMethod(method, methodInvocation)) {
                methodInvocation.setDestClass(mClassName);
                result = true;
            }
        }
        return result;
    }

    /**
     * Determines whether two given types are equal,
     * i.e. if they have the same length and signature.
     *
     * @param args1 first given type
     * @param args2 second given type
     * @return true if args1 equals args2 on mentioned conditions, else false.
     */
    private boolean compareTypes(final Type[] args1, final Type... args2) {
        boolean areEqual = args1.length == args2.length;
        if (areEqual) {
            for (int i = 0; i < args1.length; i++) {
                final String mthdInvArgSig = args2[i].getSignature();
                if (!args1[i].getSignature().equals(mthdInvArgSig)) {
                    areEqual = false;
                    break;
                }
            }
        }
        return areEqual;
    }

    /**
     * for documentation see {@link #case1}.
     */
    private void countCase1() {
        for (final FieldAccess fap : parentsReaders) {
            if (!isFieldDefinedInCurrentClass(fap.getFieldName())) {
                for (final FieldAccess fac : cClassSetters) {
                    if (fap.getFieldName().equals(fac.getFieldName())) {
                        final MethodCoupling methodCoupling = new MethodCoupling(fap.getAccessorClass().getClassName(),
                                fap.getAccessor().getName(),
                                fac.getAccessorClass().getClassName(),
                                fac.getAccessor().getName());
                        if (methodCouplings.add(methodCoupling)) {
                            case1++;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * for documentation see {@link #case2}.
     */
    private void countCase2() {
        for (final MethodInvokation mi : invFormParents) {
            if (invokedByParents(mi)) {
                if (mi.isNotConstructorInvocation()) {
                    if (callsRedefinedMethod(mi)) {
                        final MethodCoupling methodCoupling = new MethodCoupling(mi.getDestClass(), mi.getDestMethod(),
                                mi.getSrcClass(), mi.getSrcMethod());
                        if (methodCouplings.add(methodCoupling)) {
                            case2++;
                        }
                    }
                }
            }
        }
    }

    /**
     * for documentation see {@link #case3}.
     */
    private void countCase3() {
        boolean isFromParents = false;
        for (final MethodInvokation mi : invFromCClass) {
            if (mi.isNotConstructorInvocation() && !isRedefinedInCurrentClass(mi)) {
                for (int i = 0; i < parentMethods.size(); i++) {
                    final Method[] get = parentMethods.get(i);
                    int pointer = 0;
                    while (pointer < get.length) {
                        final Method method = get[pointer];
                        isFromParents = isInvocationOfTheMethod(method, mi);
                        if (isFromParents) {
                            mi.setDestClass(parents[i].getClassName());
                            final MethodCoupling methodCoupling = new MethodCoupling(mi.getDestClass(), mi.getDestMethod(),
                                    mi.getSrcClass(), mi.getSrcMethod());
                            if (methodCouplings.add(methodCoupling)) {
                                break;
                            }
                        }
                        pointer++;
                    }
                    if (isFromParents) {
                        break;
                    }
                }
                if (isFromParents) {
                    case3++;
                }
            }
        }
    }

    /**
     * Determines whether two methods are equal,
     * i.e. when they have the same name and the same set of arguments.
     *
     * @return true if m equals pm, else false
     */
    private boolean equalMethods(final Method method, final Method method1) {
        boolean result = false;
        if (method.getName().equals(method1.getName())) {
            result = compareTypes(method.getArgumentTypes(), method.getArgumentTypes());
        }
        return result;
    }

    /**
     * Investigates method - a member of the currently investigated class.
     *
     * @param method the given method
     */
    private void investigateMethod(final Method method) {
        final MethodGen methodGen = new MethodGen(method, mClassName, mPoolGen);
        if (!methodGen.isAbstract() && !methodGen.isNative()) {
            for (InstructionHandle ih = methodGen.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
                final Instruction instruction = ih.getInstruction();
                if (instructionNotVisited(instruction)) {
                    instruction.accept(new EmptyVisitor() {

                        @Override
                        public void visitInvokeInstruction(final InvokeInstruction invokeInstruction) {
                            String methodName;
                            String className;
                            final Type[] args = invokeInstruction.getArgumentTypes(mPoolGen);
                            methodName = invokeInstruction.getMethodName(mPoolGen);
                            className = invokeInstruction.getClassName(mPoolGen);

                            if (args.length > 0) {
                                final MethodInvokation methodInvokation = new MethodInvokation(className, methodName, args, mClassName, method.getName(), method.getArgumentTypes());
                                invFromCClass.add(methodInvokation);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * Investigates method (and looks for setters)
     * a member of the currently investigated class.
     * @param method the given method
     */
    private void investigateMethodAndLookForSetters(final Method method) {
        final MethodGen methodGen = new MethodGen(method, mClassName, mPoolGen);
        if (!methodGen.isAbstract() && !methodGen.isNative()) {
            for (InstructionHandle ih = methodGen.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
                final Instruction instruction = ih.getInstruction();
                if (instructionNotVisited(instruction)) {
                    instruction.accept(new EmptyVisitor() {

                        @Override
                        public void visitFieldInstruction(final FieldInstruction fieldInstruction) {

                            if (isSetInstruction(fieldInstruction)) {
                                cClassSetters.add(new FieldAccess(fieldInstruction.getFieldName(mPoolGen), method, currentClass));
                            }
                        }

                        private boolean isSetInstruction(final FieldInstruction fieldInstruction) {
                            return fieldInstruction.toString(currentClass.getConstantPool()).startsWith("put");
                        }
                    });
                }
            }
        }
    }

    /**
     * Determines whether a given field is defined in the current class.
     *
     * @param fieldName the name of the field to be checked
     * @return true if given field is defined in {@link #currentClass}, else false
     */
    private boolean isFieldDefinedInCurrentClass(final String fieldName) {
        return Arrays.stream(currentClass.getFields()).anyMatch(f -> f.getName().equals(fieldName));
    }

    /**
     * Determines whether a given method invocation comes from parents.
     *
     * @param methodInvocation the given method invocation
     * @return true if it was invoked by parents, else false.
     */
    private boolean invokedByParents(final MethodInvokation methodInvocation) {
        return Arrays.stream(parents).anyMatch(jc -> jc.getClassName().equals(methodInvocation.getDestClass()));
    }

    /**
     * It compares the method's names and the lists of method's arguments.
     * Determines whether a given method invocation comes from method method.
     *
     * @param method                given method
     * @param methodInvocation the given method invocation
     * @return true if it was invoked by method, else false.
     */
    private boolean isInvocationOfTheMethod(final Method method, final MethodInvokation methodInvocation) {
        boolean result = false;
        if (method.getName().equals(methodInvocation.getDestMethod())) {
            final Type[] args = method.getArgumentTypes();
            result =  compareTypes(args, methodInvocation.getDestMethodArgs());
        }
        return result;
    }

    /**
     * Determines whether method method has already been defined in parent class.
     *
     * @param method the given method
     * @return true if defined in parent, else false.
     */
    private boolean hasBeenDefinedInParentToo(final Method method) {
        boolean result = parentMethods.stream().flatMap(Arrays::stream).anyMatch(pm -> equalMethods(method, pm));
        final String name = method.getName();
        if ("<init>".equals(name) || "<clinit>".equals(name)) {
            result =  false;
        }

        return result;
    }

    /**
     * Determines whether method methodInvocation is redefined in current class.
     *
     * @param methodInvocation the given method invocation
     * @return true if redefined in current class, else false.
     */
    private boolean isRedefinedInCurrentClass(final MethodInvokation methodInvocation) {
        boolean result = false;
        for (final Method m : methods) {
            if (isInvocationOfTheMethod(m, methodInvocation)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * saves the generated results to {@link #mClassMetrics}
     */
    private void saveResults() {
        final int sum = case1 + case2 + case3;
        mClassMetrics.setCbm(sum);
        final Set<String> coupledParents = new TreeSet<>();

        for (final MethodCoupling mc : methodCouplings) {
            if (mc.getClassA().equals(mClassName)) {
                coupledParents.add(mc.getClassB());
                if (mc.getClassB().equals(mClassName)) {
                    LoggerHelper.printError("Both of the involved in MethodCoupling classes are the investigated class!", new RuntimeException());
                }
            } else {
                coupledParents.add(mc.getClassA());
                if (!mc.getClassB().equals(mClassName)) {
                    LoggerHelper.printError("None  of the involved in MethodCoupling classes is the investigated class!", new RuntimeException());
                }
            }
        }
        mClassMetrics.setIc(coupledParents.size());
    }


    /**
     * Determines whether a single instruction was not visited.
     *
     * @param instruction the given instruction
     * @return true if instruction was not visited, else false.
     */
    private boolean instructionNotVisited(final Instruction instruction) {
        final short opcode = instruction.getOpcode();
        return InstructionConst.getInstruction(opcode) == null;
    }

    /**
     * Getter method for parent's class name.
     *
     * @return the parent class name as a String
     */
    private String getParentClassName() {
        return parent.getClassName();
    }

}