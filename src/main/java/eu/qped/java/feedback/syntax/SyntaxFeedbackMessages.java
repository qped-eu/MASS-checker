package eu.qped.java.feedback.syntax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.NEW_Double_LINE;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackMessages {
    public Map<String, String> getFeedbackMessagesByErrorCode() {
        return new HashMap<>() {{
            put("compiler.err.already.defined"
                    , ""
                            + "The compiler expects that you don't define variables or methods twice, where they can be called (scope of that variable or method)."
            );
            put("compiler.err.not.stmt"
                    , ""
                            + "The compiler expects that you write a statement, but instead you wrote something different."
            );
            put("compiler.err.unclosed.str.lit"
                    , ""
                            + "The compiler expects that you close string literal always with \"."
            );
            put("compiler.err.else.without.if"
                    , ""
                            + "The compiler expects that else statement always come after if statement."
            );
            put("compiler.err.missing.ret.stmt"
                    , ""
                            + "Every method who has return type need a return statement at the end."
            );
            put("compiler.err.unreachable.stmt"
                    , ""
                            + "The compiler expects that you not write any code after the return statement."
            );
            // new
            put("compiler.err.var.might.not.have.been.initialized"
                    , ""
                            + "The variable not only had to be declared but also initialized"
            );
            put("compiler.err.cant.resolve.location"
                    , ""
                            + "You have called an undefined symbol (variable), at the above-mentioned position."
                            + NEW_Double_LINE
                            + "it could be that you made a typo with the name or forgot to define the symbol."
            );
            put("compiler.err.abstract.cant.be.instantiated"
                    , ""
                            + "No object could be created from abstract classes."
                            + NEW_Double_LINE
                            + "it is possible to create an object of a subclass of an abstract class"
            );
            put("compiler.err.repeated.modifier"
                    , ""
                            + "Dont repeat modifiers"
            );
            put("compiler.err.illegal.combination.of.modifiers"
                    , ""
                            + "Modifiers are additional properties for Java declarations such as methods, variables and classes"
                            + NEW_Double_LINE
                            + "you always have to specify it at the very beginning of the declaration"
                            + NEW_Double_LINE
                            + "you can also combine them but unfortunately not like what you have done"
                            + NEW_Double_LINE
                            + "You can combine almost all modifiers with static, but you are not allowed to combine it public with itself or with private or protected"
            );
            put("compiler.err.missing.meth.body.or.decl.abstract"
                    , ""
                            + "The declaration of a method consists of 2 steps:"
                            + NEW_Double_LINE
                            + "Method header: <return type> methodName()"
                            + NEW_Double_LINE
                            + "Method body: {code block and a return if necessary}"
            );
            put("compiler.err.illegal.start.of.expr"
                    , ""
                            + "The compiler expects that you write legal expression."
                            + NEW_Double_LINE
                            + "This usually happens when you:"
                            + NEW_Double_LINE
                            + "1. write method or class inside of another method"
                            + NEW_Double_LINE
                            + "2. write method or class inside of another method"
                            + NEW_Double_LINE
                            + "3. forget to write a curly braces \"{\" or \"}\" "
            );
        }};
    }

    public Map<String, String> getFeedbackMessagesByErrorMessage() {
        return new HashMap<>() {{
            put("';' expected"
                    , ""
                            + "The compiler expects that you end statements with a \";\""
                            + NEW_Double_LINE
                            + "This usually happens when you forget to write semicolon or closing parenthesis."
            );
            put("'(' expected"
                    , ""
                            + "The compiler expects that you write a \"(\""
                            + NEW_Double_LINE
                            + "This usually happens when you forget to write a left parenthesis in your code."
            );
            put("')' expected"
                    , ""
                            + "The compiler expects that you write a \")\""
                            + NEW_Double_LINE
                            + "This usually happens when you forget to write a right parenthesis in your code."
            );
            put("<identifier> expected"
                    , ""
                            + "The compiler expects that you write a \"identifier\""
                            + NEW_Double_LINE
                            + "This usually happens when you want to define a variable without a name or write code somewhere where Java does not expect."
            );
            put("class, interface, or enum expected"
                    , ""
                            + "The compiler expects that you create a \"class\", \"interface\" or \"enum\"."
                            + NEW_Double_LINE
                            + "This usually happens when you want to write a method outside of the class or or write additional curly bracket \"}\"."
            );
        }};
    }
}
