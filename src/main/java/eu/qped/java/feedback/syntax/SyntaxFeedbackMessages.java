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
                            + "The compiler expects that you initialize a variable."
                            + NEW_Double_LINE
                            + "This mean you must give this variable a Value."
            );
            put("compiler.err.abstract.cant.be.instantiated"
                    , ""
                            + "The compiler expects that you not create an instance from an abstract class."
                            + NEW_Double_LINE
                            + "But you can create an object of a subclass of this abstract class."
            );
            put("compiler.err.repeated.modifier"
                    , ""
                            + "The compiler expects that you not repeat modifiers like “public”, “private”, ..."
            );
            put("compiler.err.illegal.combination.of.modifiers"
                    , ""
                            + "The compiler expects that combine modifiers correctly."
                            + NEW_Double_LINE
                            + "You can only combine static and final with private, public and protected."
            );
            put("compiler.err.missing.meth.body.or.decl.abstract"
                    , ""
                            + "The compiler expects that you declare method correctly."
                            + NEW_Double_LINE
                            + "To fix this, you need to declare a method header like \"<return type> methodName()\" and method body like \"{ some code }\")."
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
            put("compiler.err.illegal.start.of.type"
                    , ""
                            + "The compiler expects that you write code in right place."
                            + NEW_Double_LINE
                            + "This usually happens when you write for, if, else, ... in a wrong place "
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
