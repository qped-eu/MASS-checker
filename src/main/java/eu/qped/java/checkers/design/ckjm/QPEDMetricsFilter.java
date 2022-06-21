package eu.qped.java.checkers.design.ckjm;

import gr.spinellis.ckjm.*;
import gr.spinellis.ckjm.utils.LoggerHelper;
import lombok.RequiredArgsConstructor;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convert a list of classes into their metrics.
 * Given metrics are defined in {@link DesignCheckEntryHandler.Metric}.
 *
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a> (from CKJM-extended tool)
 * @author Jannik Seus (edited)
 */
@RequiredArgsConstructor
public class QPEDMetricsFilter implements ICountingProperities {

    /**
     * True if the measurements should include calls to the Java JDK into account
     */
    private boolean includeJdk = false;

    /**
     * True if the reports should only include public classes.
     */
    private boolean onlyPublic = false;

    /**
     * The same instance of MoaClassVisitor must be used to process all class, so it must be a class field.
     */
    private final MoaClassVisitor moaVisitor;

    /**
     * container for available metrics
     */
    private final IClassMetricsContainer metricsContainer;

    public QPEDMetricsFilter() {
        metricsContainer = new ClassMetricsContainer(this);
        moaVisitor = new MoaClassVisitor(metricsContainer);
    }

    /**
     * The interface for other Java based applications.
     * implement the output handler to catch the results
     *
     * @param files class files to be analyzed
     * @param outputHandler an implementation of the CkjmOutputHandler interface
     */
    public static void runMetrics(List<String> files, CkjmOutputHandler outputHandler, boolean includeJDK) {
        QPEDMetricsFilter qmf = new QPEDMetricsFilter();
        qmf.includeJdk = includeJDK;

        qmf.runMetricsInternal(files, outputHandler);
    }

    /**
     * Return true if the measurements should include calls to the Java JDK into account
     */
    public boolean isJdkIncluded() {
        return includeJdk;
    }

    /**
     * @return true if the measurements should include all classes
     */
    public boolean includeAll() {
        return !onlyPublic;
    }

    public void setIncludeJdk(boolean mIncludeJdk) {
        this.includeJdk = mIncludeJdk;
    }

    public void setOnlyPublic(boolean mOnlyPublic) {
        this.onlyPublic = mOnlyPublic;
    }

    /**
     * Load and parse the specified class.
     * The class specification can be either a class file name, or
     * a jarfile, followed by space, followed by a class file name.
     */
    void processClass(String clspec) {
        JavaClass jc = null;

        if (clspec.toLowerCase().endsWith(".jar")) {
            JarFile jf;
            try {
                jf = new JarFile(clspec);
                Enumeration<JarEntry> entries = jf.entries();

                while (entries.hasMoreElements()) {
                    String cl = entries.nextElement().getName();
                    if (cl.toLowerCase().endsWith(".class")) {
                        try {
                            jc = new ClassParser(clspec, cl).parse();
                            processClass(jc);
                        } catch (IOException e) {
                            LoggerHelper.printError("Error loading " + cl + " from " + clspec + ": " + e);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(QPEDMetricsFilter.class.getName()).log(Level.SEVERE, "Unable to load jar file " + clspec, ex);
            }
        } else {
            try {
                jc = new ClassParser(clspec).parse();
                processClass(jc);
            } catch (IOException e) {
                LoggerHelper.printError("Error loading " + clspec + ": " + e);
            }
        }
    }

    /**
     * Processes a given class using a class visitor.
     *
     * @param javaClass the given class
     */
    private void processClass(JavaClass javaClass) {
        if (javaClass != null) {
            ClassVisitor visitor = new ClassVisitor(javaClass, metricsContainer, this);
            visitor.start();
            visitor.end();
            LocClassVisitor locVisitor = new LocClassVisitor(metricsContainer);
            locVisitor.visitJavaClass(javaClass);
            DamClassVisitor damVisitor = new DamClassVisitor(javaClass, metricsContainer);
            damVisitor.visitJavaClass(javaClass);
            moaVisitor.visitJavaClass(javaClass);
            MfaClassVisitor mfaVisitor = new MfaClassVisitor(metricsContainer);
            mfaVisitor.visitJavaClass(javaClass);
            CamClassVisitor camVisitor = new CamClassVisitor(metricsContainer);
            camVisitor.visitJavaClass(javaClass);
            QPEDIcAndCbmClassVisitor icVisitor = new QPEDIcAndCbmClassVisitor(metricsContainer);
            icVisitor.visitJavaClass(javaClass);
            AmcClassVisitor amcVisitor = new AmcClassVisitor(metricsContainer);
            amcVisitor.visitJavaClass(javaClass);
        } else {
            LoggerHelper.printError("Given class ist null.");
        }
    }

    /**
     * The interface for other Java based applications.
     * Implement the output handler to catch the results
     *
     * @param files         class files to be analyzed
     * @param outputHandler an implementation of the CkjmOutputHandler interface
     */
    public void runMetricsInternal(List<String> files, CkjmOutputHandler outputHandler) {

        for (String file : files) {
            processClass(file);
        }
        moaVisitor.end();
        metricsContainer.printMetrics(outputHandler);
    }
}
