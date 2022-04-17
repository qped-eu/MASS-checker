package eu.qped.java.utils.compiler;

import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;


public class JavaObjectFromString extends SimpleJavaFileObject {

    protected String content;

    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     * URI ist the representation of the FileObject.
     * The Kind is a special Type that tells what kind/Type is the Object.
     * Kind.SOURCE because we are Converting a java source Object (String).
     */
    public JavaObjectFromString(String className, String content) throws URISyntaxException {
        super(new URI(className) , JavaFileObject.Kind.SOURCE);
        this.content = content;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.content;
    }
}
