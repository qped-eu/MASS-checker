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

 Custom visitor class for the metrics:
 IC {@link Metric#IC} and
 CBM {@link Metric#CBM}.
 <p>This class extends the {@link AbstractClassVisitor} and is responsible for calculating the IC and CBM metrics.
 The IC metric measures the number of methods that are invoked by a class, and the CBM metric measures the number of methods
 that are invoked by a class and use the return value of the invoked method.
 <p>The class uses several variables to keep track of method invocations, field access, and method couplings.
 The {@link #case1}, {@link #case2} and {@link #case3} variables are used to keep track of specific cases when
 inherited methods use a field or call a redefined method.
 @author marian (from CKJM-extended tool)
 @author Jannik Seus (edited)
 */
public class QPEDIcAndCbmClassVisitor extends AbstractClassVisitor {

    /**

     The array of methods for the current class being visited.
     */
     private Method[] methods;
     /**

     The current class being visited.
     */
     private JavaClass currentClass;
     /**
     The parent pool for the current class being visited.
     */
     private ConstantPoolGen parentPool;
     /**
     The parent methods for the current class being visited.
     */
     private List<Method[]> parentMethods;
     /**
     The method invocations from parents for the current class being visited.
     */
     private Set<MethodInvokation> invFormParents;
     /**
      The method invocations from the current class being visited.
      */
     private Set<MethodInvokation> invFromCClass;
     /**
     The parents readers for the current class being visited.
     */
     private Set<FieldAccess> parentsReaders;
     /**
     The current class setters for the current class being visited.
     */
     private Set<FieldAccess> cClassSetters;
     /**
     The method couplings for the current class being visited.
     */
     private Set<MethodCoupling> methodCouplings;
     /**
     The parents for the current class being visited.
     */
     private JavaClass[] parents;
     /**
     The parent for the current class being visited.
     */
    private JavaClass parent;
    /**

     The variable that determines how many inherited methods use a field, that is defined
     in a new/redefined method.
     */
    private int case1;

    /**
     * Determines how many inherited methods call a redefined method
     * and use the return value of the redefined method.
     */
    private int case2;

    /**
     * Determines how many inherited methods are called by a redefined method
     * and use a parameter that is defined in the redefined method.
     */
    private int case3;

    /**
     * Constructor
     * @param classMap is passed to the constructor of the parent class and used to store the computed metrics
     */
    public QPEDIcAndCbmClassVisitor(final IClassMetricsContainer classMap) {
        super(classMap);
    }

    /**
     * Visits the body of a Java class and calculates the IC and CBM metrics.
     *
     * @param javaClass the Java class to visit
     * The method first initializes the variables case1, case2 and case3 to zero, sets the currentClass to javaClass.
     * Then, it gets the superclass of the current class, initializes the parentMethods list,
     * gets the methods of the current class, creates new TreeSet for invFormParents, invFromCClass, parentsReaders,
     * cClassSetters and methodCouplings.
     * Then, it loops through the parents and for each parent it gets the methods, and accepts the method,
     * then for each method in methods, it checks if the method has been defined in parent, and if so,
     * it investigates the method, and looks for setters. Finally, it counts case1, case2 and case3, and saves the results.
     * If an exception is thrown, it throws an IllegalArgumentException.
     */
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
     * This method will look for the invocation of methods and the reading of fields in the parent methods,
     * and store that information in the {@link #invFormParents} and {@link #parentsReaders} sets, respectively.
     * @param method the method of the parent class that is being visited
     */
    @Override
    public void visitMethod(final Method method) {
        final MethodGen methodGen = new MethodGen(method, getParentClassName(), parentPool);
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

                            final MethodInvokation methodInvokation = new MethodInvokation(className, methodName, args, getParentClassName(), method.getName(), method.getArgumentTypes());
                            invFormParents.add(methodInvokation);

                        }

                        @Override
                        public void visitFieldInstruction(final FieldInstruction fieldInstruction) {
                            if (isGetInstruction(fieldInstruction)) {
                                handleGetInstruction(fieldInstruction);
                            }
                        }

                        private void handleGetInstruction(final FieldInstruction fieldInstruction) {
                            final FieldAccess fieldAccess = new FieldAccess(fieldInstruction.getFieldName(parentPool), method, parent);
                            parentsReaders.add(fieldAccess);
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
     * Determines whether a given {@link MethodInvokation} is a call of a redefined method in the investigated class.
     *
     * @param methodInvocation the given method invocation to be checked
     * @return true when the methodInvocation is an invocation of a method that has been redefined in the investigated class,
     * false otherwise.
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
     * Determines whether two given types are equal, i.e. if they have the same length and signature.
     *
     * @param args1 The first given type to compare.
     * @param args2 The second given type to compare.
     * @return true if args1 and args2 have the same length and signature, false otherwise.
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
     * Counts the number of cases where inherited methods use a field defined in a new/redefined method.
     * This is done by comparing the parents' readers and the current class setters.
     * If a parent method reads a field that is defined in a new/redefined method, the method coupling is added to the set and case1 is incremented.
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
     * Counts the number of inherited methods that call a redefined method
     * and use the return value of the redefined method.
     * This value is stored in the {@code case2} variable.
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
     * This method counts the number of cases where inherited methods call a redefined method and use the
     * parameter that is defined in the redefined method.
     * The method iterates through all the method invocations from current class and checks if they are not constructor
     * invocations and not redefined in current class. Then it iterates through all the parent methods and checks if
     * the current method invocation is an invocation of that method. If it is, it sets the destination class to the
     * class of that parent method. Then it creates a new MethodCoupling object with the source and destination class and
     * methods and adds it to the methodCouplings set. If the methodCoupling is successfully added, it increments the
     * case3 counter.
     */
    private void countCase3() {
        for (final MethodInvokation mi : invFromCClass) {
            if (mi.isNotConstructorInvocation() && !isRedefinedInCurrentClass(mi)) {
                boolean isFromParents = checkParentMethods(mi);
                if (isFromParents) {
                    case3++;
                }
            }
        }
    }

    private boolean checkParentMethods(MethodInvokation mi) {
        boolean isFromParents = false;
        for (int i = 0; i < parentMethods.size(); i++) {
            final Method[] get = parentMethods.get(i);
            if (matchParentMethod(get, mi)) {
                isFromParents = true;
                break;
            }
        }
        return isFromParents;
    }

    private boolean matchParentMethod(Method[] parentMethod, MethodInvokation mi) {
        for (int i = 0; i < parentMethod.length; i++) {
            if (isInvocationOfTheMethod(parentMethod[i], mi)) {
                mi.setDestClass(parents[i].getClassName());
                final MethodCoupling methodCoupling = new MethodCoupling(mi.getDestClass(), mi.getDestMethod(),
                        mi.getSrcClass(), mi.getSrcMethod());
                if (methodCouplings.add(methodCoupling)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compares two given methods to see if they are equal, by comparing their name and argument types.
     *
     * @param method the first method to compare
     * @param method1 the second method to compare
     * @return true if the name and argument types of the two methods are equal, false otherwise
     */
    private boolean equalMethods(final Method method, final Method method1) {
        boolean result = false;
        if (method.getName().equals(method1.getName())) {
            result = compareTypes(method.getArgumentTypes(), method.getArgumentTypes());
        }
        return result;
    }

    /**
     * Investigates the given method and looks for invocations of other methods.
     *
     * @param method the given method to investigate
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

     This method is used to investigate the given method and look for setters.
     @param method The method to investigate.
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
     * Determines whether the given method invocation was made by any of the parent classes.
     *
     * @param methodInvocation the given method invocation to check
     * @return true if the method invocation was made by any of the parent classes, else false.
     */
    private boolean invokedByParents(final MethodInvokation methodInvocation) {
        return Arrays.stream(parents).anyMatch(jc -> jc.getClassName().equals(methodInvocation.getDestClass()));
    }

    /**
     * Determines whether a given method invocation is a call of the specified method.
     *
     * @param method The method to check against
     * @param methodInvocation The method invocation to check
     * @return true if the method invocation is a call of the specified method, false otherwise.
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

     Checks if the given method has been defined in any of the parent classes of the current class.
     @param method The method to check
     @return true if the method has been defined in any of the parent classes, false otherwise.
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
     * Determines if the given method invocation is a call to a method that has been redefined in the current class.
     *
     * @param methodInvocation the method invocation to be checked
     * @return true if the method invocation is a call to a method that has been redefined in the current class,
     * false otherwise
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
     * Saves the results of the method coupling analysis by setting the calculated values for the
     * Coupling Between Methods (CBM) and the number of coupled parents (IC) in the ClassMetrics object.
     * Additionally, it validates the MethodCoupling instances and throws an error if they contain
     * both the investigated class or neither of the involved classes.
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
     * Determines whether the given instruction has not been visited before.
     *
     * @param instruction the instruction to be checked
     * @return true if the instruction has not been visited before, false otherwise.
     */
    private boolean instructionNotVisited(final Instruction instruction) {
        final short opcode = instruction.getOpcode();
        return InstructionConst.getInstruction(opcode) == null;
    }

    /**
     * Retrieves the name of the parent class.
     *
     * @return a string representing the name of the parent class.
     */
    private String getParentClassName() {
        return parent.getClassName();
    }

    public int getCase1() {
        return case1;
    }

    public int getCase2() {
        return case2;
    }

    public int getCase3() {
        return case3;
    }

    public JavaClass getCurrentClass() {
        return currentClass;
    }

    public List<Method[]> getParentMethods() {
        return parentMethods;
    }

    public Set<MethodInvokation> getInvFormParents() {
        return invFormParents;
    }

    public Set<MethodInvokation> getInvFromCClass() {
        return invFromCClass;
    }

    public Set<FieldAccess> getParentsReaders() {
        return parentsReaders;
    }

    public Set<FieldAccess> getcClassSetters() {
        return cClassSetters;
    }

    public Set<MethodCoupling> getMethodCouplings() {
        return methodCouplings;
    }
}