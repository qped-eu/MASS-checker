package eu.qped.java.utils.compiler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {


    @Test
    public void compileClassFromStringTest() {
        String classAsString =
                "public class Hallo {\n" +
                "   public String Greeting() {\n" +
                        " return \"hallo\";\n" +
                        "}\n" +
                        "}\n";

        Compiler compiler = Compiler.builder().build();
        //compiler.compile(classAsString);


    }


}