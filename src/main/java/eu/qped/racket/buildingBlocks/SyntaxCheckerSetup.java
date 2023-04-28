package eu.qped.racket.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SyntaxCheckerSetup {

    private final ArrayList<String> preDefinedFunctionsList;
    private final ArrayList<String> preDefinedVariablesList;
    private final HashMap<String, String[]> functionNameAndAttributes;
    private final HashMap<String, String> variableNameAndValue;

    public SyntaxCheckerSetup() {
        String[] preDefinedFunctions = new String[]{
                // On Numbers
                "+", "-", "*", "/", "<", "<=", "=", ">", ">=", "abs", "acos", "add1", "angle", "asin", "atan", "ceiling", "complex?", "conjugate", "cos", "cosh", "current-seconds", "denominator",
                "even?", "exact->inexact", "exact?", "exp", "expt", "floor", "gcd", "imag-part", "inexact->exact", "inexact?", "integer->char", "integer-sqrt", "integer?", "lcm", "log", "magnitude",
                "make-polar", "make-rectangular", "max", "min", "modulo", "negative?", "number->string", "number->string-digits", "number?", "numerator", "odd?", "positive?", "quotient", "random", "rational?",
                "real-part", "real?", "remainder", "round", "sgn", "sin", "sinh", "sqr", "sqrt", "sub1", "tan", "zero?",
                // on Booleans
                "boolean->string", "boolean=?", "boolean?", "false?", "not", "and", "or", "if",
                // on Symbols
                "symbol->string", "symbol=?", "symbol?",
                // on Lists
                "append", "assoc", "assq", "caaar", "caadr", "caar", "cadar", "cadddr", "caddr", "cadr", "car", "cdaar", "cdadr", "cdar", "cddar", "cdddr", "cddr", "cdr", "cons", "cons?", "eighth",
                "empty?", "fifth", "first", "fourth", "length", "list", "list*", "list-ref", "list?", "make-list", "member", "member?", "memq", "memq?", "memv", "null?", "range", "remove", "remove-all", "rest",
                "reverse", "second", "seventh", "sixth", "third",
                // on Posns
                "make-posn", "posn-x", "posn-y", "posn?",
                // on Characters
                "char->integer", "char-alphabetic?", "char-ci<=?", "char-ci<?", "char-ci=?", "char-ci>=?", "char-ci>?", "char-downcase", "char-lower-case?", "char-numeric?", "char-upcase",
                "char-upper-case?", "char-whitespace?", "char<=?", "char<?", "char=?", "char>=?", "char>?", "char?",
                // on Strings
                "explode", "format", "implode", "int->string", "list->string", "make-string", "replicate", "string", "string->int", "string->list", "string->number", "string->symbol", "string-alphabetic?",
                "string-append", "string-ci<=?", "string-ci<?", "string-ci=?", "string-ci>=?", "string-ci>?", "string-contains-ci?", "string-contains?", "string-copy", "string-downcase", "string-ith",
                "string-length", "string-lower-case?", "string-numeric?", "string-ref", "string-upcase", "string-upper-case?", "string-whitespace?", "string<=?", "string<?", "string=?", "string>=?",
                "string>?", "string?", "substring",
                // special
                "cond"};
        preDefinedFunctionsList = new ArrayList<>(Arrays.asList(preDefinedFunctions));

        String[] preDefinedVariables = new String[]{"empty", "pi", "null", "e"};
        preDefinedVariablesList = new ArrayList<>(Arrays.asList(preDefinedVariables));

        functionNameAndAttributes = new HashMap<>();

        variableNameAndValue = new HashMap<>();

        // special
        String[] parameterANYNameINIFINITE = {"ANY", "Name", "INFINITE"};
        functionNameAndAttributes.put("cond", parameterANYNameINIFINITE);

        // pre-defined Variables
        variableNameAndValue.put("empty", "List");
        variableNameAndValue.put("pi", "Number");
        variableNameAndValue.put("null", "List");
        variableNameAndValue.put("e", "HashName");

        // On numbers
        String[] parameterNumberNumberNumberInfinite = {"Number", "Number", "Number", "INFINITE"};
        String[] parameterBooleanNumberNumberInfinite = {"Boolean", "Number", "Number", "INFINITE"};
        String[] parameterNumberNumberINFINITE = {"Number", "Number", "INFINITE"};
        String[] parameterNumberNumber = {"Number", "Number"};
        String[] parameterBooleanNumber = {"Boolean", "Number"};
        String[] parameterNumber = {"Number"};
        String[] parameterHashnameNumber = {"HashName", "Number"};
        String[] parameterNumberNumberNumber = {"Number", "Number", "Number"};
        String[] parameterCharacterNumber = {"Character", "Number"};
        String[] parameterBooleanANY = {"Boolean", "ANY"};
        String[] parameterHashnameHashname = {"HashName", "HashName"};
        String[] parameterHashnameNumberNumber = {"HashName", "Number", "Number"};
        String[] parameterStringNumber = {"String", "Number"};
        String[] parameterStringNumberNumber = {"String", "Number", "Number"};
        functionNameAndAttributes.put("+", parameterNumberNumberNumberInfinite);
        functionNameAndAttributes.put("*", parameterNumberNumberNumberInfinite);
        functionNameAndAttributes.put("/", parameterNumberNumberNumberInfinite);
        functionNameAndAttributes.put("-", parameterNumberNumberINFINITE);
        functionNameAndAttributes.put("<", parameterBooleanNumberNumberInfinite);
        functionNameAndAttributes.put("<=", parameterBooleanNumberNumberInfinite);
        functionNameAndAttributes.put(">", parameterBooleanNumberNumberInfinite);
        functionNameAndAttributes.put(">=", parameterBooleanNumberNumberInfinite);
        functionNameAndAttributes.put("=", parameterBooleanNumberNumberInfinite);
        functionNameAndAttributes.put("abs", parameterNumberNumber);
        functionNameAndAttributes.put("acos", parameterNumberNumber);
        functionNameAndAttributes.put("add1", parameterNumberNumber);
        functionNameAndAttributes.put("angle", parameterNumberNumber);
        functionNameAndAttributes.put("asin", parameterNumberNumber);
        functionNameAndAttributes.put("atan", parameterNumberNumber);
        functionNameAndAttributes.put("ceiling", parameterNumberNumber);
        functionNameAndAttributes.put("complex?", parameterBooleanANY);
        functionNameAndAttributes.put("conjugate", parameterNumberNumber);
        functionNameAndAttributes.put("cos", parameterNumberNumber);
        functionNameAndAttributes.put("cosh", parameterNumberNumber);
        functionNameAndAttributes.put("current-seconds", parameterNumber);
        functionNameAndAttributes.put("denominator", parameterNumberNumber);
        functionNameAndAttributes.put("even?", parameterBooleanNumber);
        functionNameAndAttributes.put("exact->inexact", parameterNumberNumber);
        functionNameAndAttributes.put("exact?", parameterBooleanNumber);
        functionNameAndAttributes.put("exp", parameterHashnameNumber);
        functionNameAndAttributes.put("expt", parameterNumberNumberNumber);
        functionNameAndAttributes.put("floor", parameterNumberNumber);
        functionNameAndAttributes.put("gcd", parameterNumberNumberINFINITE);
        functionNameAndAttributes.put("imag-part", parameterNumberNumber);
        functionNameAndAttributes.put("inexact->exact", parameterNumberNumber);
        functionNameAndAttributes.put("inexact?", parameterBooleanNumber);
        functionNameAndAttributes.put("integer->char", parameterCharacterNumber);
        functionNameAndAttributes.put("integer-sqrt", parameterNumberNumber);       // complex ist Name !!!!!!!!!!!!
        functionNameAndAttributes.put("integer?", parameterBooleanANY);
        functionNameAndAttributes.put("lcm", parameterNumberNumberINFINITE);
        functionNameAndAttributes.put("log", parameterHashnameNumber);
        functionNameAndAttributes.put("magnitude", parameterHashnameHashname);
        functionNameAndAttributes.put("make-polar", parameterHashnameNumberNumber);
        functionNameAndAttributes.put("make-rectangular", parameterHashnameNumberNumber);
        functionNameAndAttributes.put("max", parameterNumberNumberINFINITE);
        functionNameAndAttributes.put("min", parameterNumberNumberINFINITE);
        functionNameAndAttributes.put("modulo", parameterNumberNumberNumber);
        functionNameAndAttributes.put("negative?", parameterBooleanNumber);
        functionNameAndAttributes.put("number->string", parameterStringNumber);
        functionNameAndAttributes.put("number->string-digits", parameterStringNumberNumber);
        functionNameAndAttributes.put("number?", parameterBooleanANY);
        functionNameAndAttributes.put("numerator", parameterNumberNumber);
        functionNameAndAttributes.put("odd?", parameterBooleanNumber);
        functionNameAndAttributes.put("positive?", parameterBooleanNumber);
        functionNameAndAttributes.put("quotient", parameterNumberNumberNumber);
        functionNameAndAttributes.put("random", parameterNumberNumber);
        functionNameAndAttributes.put("rational?", parameterBooleanANY);
        functionNameAndAttributes.put("real-part", parameterNumberNumber);
        functionNameAndAttributes.put("real?", parameterBooleanANY);
        functionNameAndAttributes.put("remainder", parameterNumberNumberNumber);
        functionNameAndAttributes.put("round", parameterNumberNumber);
        functionNameAndAttributes.put("sgn", parameterNumberNumber);
        functionNameAndAttributes.put("sin", parameterNumberNumber);
        functionNameAndAttributes.put("sinh", parameterNumberNumber);
        functionNameAndAttributes.put("sqr", parameterNumberNumber);
        functionNameAndAttributes.put("sqrt", parameterNumberNumber);
        functionNameAndAttributes.put("sub1", parameterNumberNumber);
        functionNameAndAttributes.put("tan", parameterNumberNumber);
        functionNameAndAttributes.put("zero?", parameterBooleanNumber);


        // on Booleans
        String[] parameterStringBoolean = {"String", "Boolean"};
        String[] parameterBooleanBooleanBoolean = {"Boolean", "Boolean", "Boolean"};
        String[] parameterBooleanBoolean = {"Boolean", "Boolean"};
        functionNameAndAttributes.put("boolean->string", parameterStringBoolean);
        functionNameAndAttributes.put("boolean=?", parameterBooleanBooleanBoolean);
        functionNameAndAttributes.put("boolean?", parameterBooleanANY);
        functionNameAndAttributes.put("false?", parameterBooleanANY);
        functionNameAndAttributes.put("not", parameterBooleanBoolean);

        String[] parameterBooleanBooleanBooleanINFNITE = {"Boolean", "Boolean", "Boolean", "INFINITE"};
        String[] parameterANYBooleanANYANY = {"ANY", "Boolean", "ANY", "ANY"};
        functionNameAndAttributes.put("and", parameterBooleanBooleanBooleanINFNITE);
        functionNameAndAttributes.put("or", parameterBooleanBooleanBooleanINFNITE);
        functionNameAndAttributes.put("if", parameterANYBooleanANYANY);


        // on Symbols
        String[] parameterStringSymbol = {"String", "Symbol"};
        String[] parameterBooleanSymbolSymbol = {"Boolean", "Symbol", "Symbol"};
        functionNameAndAttributes.put("symbol->string", parameterStringSymbol);
        functionNameAndAttributes.put("symbol=?", parameterBooleanSymbolSymbol);
        functionNameAndAttributes.put("symbol?", parameterBooleanANY);


        // on Lists
        String[] parameterListListListINFINITE = new String[]{"List", "List", "List", "INFINITE"};
        String[] parameterListANYList = new String[]{"List", "ANY", "List"};
        String[] parameterANYList = {"ANY", "List"};
        String[] parameterListANYINFINITEList = {"List", "ANY", "INFINITE", "List"};
        String[] parameterNumberList = {"Number", "List"};
        String[] parameterListANYINFINITE = {"List", "ANY", "INFINITE"};
        String[] parameterBooleanANYList = {"Boolean", "ANY", "List"};
        String[] parameterListNumberNumberNumber = {"List", "Number", "Number", "Number"};
        String[] parameterANYListNumber = {"ANY", "List", "Number"};
        String[] parameterListNumberANY = {"List", "Number", "ANY"};
        String[] parameterListList = {"List", "List"};
        functionNameAndAttributes.put("append", parameterListListListINFINITE);
        functionNameAndAttributes.put("assoc", parameterListANYList);    // maybe not checkable -->
        functionNameAndAttributes.put("assq", parameterListANYList);
        functionNameAndAttributes.put("caaar", parameterANYList);
        functionNameAndAttributes.put("caadr", parameterANYList);
        functionNameAndAttributes.put("caar", parameterANYList);
        functionNameAndAttributes.put("cadar", parameterANYList);
        functionNameAndAttributes.put("cadddr", parameterANYList);
        functionNameAndAttributes.put("caddr", parameterANYList);
        functionNameAndAttributes.put("cadr", parameterANYList);     // <-- maybe not checkable, has to be checked if the program is executed
        functionNameAndAttributes.put("car", parameterANYList);
        functionNameAndAttributes.put("cdaar", parameterANYList);    // maybe not checkable in syntaxCheck -->
        functionNameAndAttributes.put("cdadr", parameterANYList);
        functionNameAndAttributes.put("cdar", parameterListList);
        functionNameAndAttributes.put("cddar", parameterANYList);
        functionNameAndAttributes.put("cdddr", parameterANYList);
        functionNameAndAttributes.put("cddr", parameterListList);
        functionNameAndAttributes.put("cdr", parameterANYList);      // <-- maybe not checkable in syntaxCheck
        functionNameAndAttributes.put("cons", parameterListANYList);
        functionNameAndAttributes.put("cons?", parameterBooleanANY);
        functionNameAndAttributes.put("eighth", parameterANYList);
        functionNameAndAttributes.put("empty?", parameterBooleanANY);
        functionNameAndAttributes.put("fifth", parameterANYList);
        functionNameAndAttributes.put("first", parameterANYList);
        functionNameAndAttributes.put("fourth", parameterANYList);
        functionNameAndAttributes.put("length", parameterNumberList);
        functionNameAndAttributes.put("list", parameterListANYINFINITE);
        functionNameAndAttributes.put("list*", parameterListANYINFINITEList);        // Hier gibts noch ein großes Problem
        functionNameAndAttributes.put("list-ref", parameterANYListNumber);
        functionNameAndAttributes.put("list?", parameterBooleanANY);
        functionNameAndAttributes.put("make-list", parameterListNumberANY);
        functionNameAndAttributes.put("member", parameterBooleanANYList);
        functionNameAndAttributes.put("member?", parameterBooleanANYList);
        functionNameAndAttributes.put("memq", parameterBooleanANYList);
        functionNameAndAttributes.put("memq?", parameterBooleanANYList);
        functionNameAndAttributes.put("memv", parameterBooleanANYList);         // kann sowohl Boolean als auch List ausgeben
        functionNameAndAttributes.put("null?", parameterBooleanANY);
        functionNameAndAttributes.put("range", parameterListNumberNumberNumber);
        functionNameAndAttributes.put("remove", parameterListANYList);
        functionNameAndAttributes.put("remove-all", parameterListANYList);
        functionNameAndAttributes.put("rest", parameterANYList);
        functionNameAndAttributes.put("reverse", parameterListList);
        functionNameAndAttributes.put("second", parameterANYList);
        functionNameAndAttributes.put("seventh", parameterANYList);
        functionNameAndAttributes.put("sixth", parameterANYList);
        functionNameAndAttributes.put("third", parameterANYList);


        // on Posns
        String[] parameterPosnANYANY = {"Name", "ANY", "ANY"};
        String[] parameterANYPosn = {"ANY", "Name"};
        functionNameAndAttributes.put("make-posn", parameterPosnANYANY);
        functionNameAndAttributes.put("posn-x", parameterANYPosn);
        functionNameAndAttributes.put("posn-y", parameterANYPosn);
        functionNameAndAttributes.put("posn?", parameterBooleanANY);


        // on Characters
        String[] parameterNumberCharacter = {"Number", "Character"};
        String[] parameterBooleanCharacter = {"Boolean", "Character"};
        String[] parameterBooleanCharacterINFINITE = {"Boolean", "Character", "INFINITE"};
        String[] parameterCharacterCharacter = {"Character", "Character"};
        functionNameAndAttributes.put("char->integer", parameterNumberCharacter);
        functionNameAndAttributes.put("char-alphabetic?", parameterBooleanCharacter);
        functionNameAndAttributes.put("char-ci<=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char-ci<?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char-ci=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char-ci>=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char-ci>?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char-downcase", parameterCharacterCharacter);
        functionNameAndAttributes.put("char-lower-case?", parameterBooleanCharacter);
        functionNameAndAttributes.put("char-numeric?", parameterBooleanCharacter);
        functionNameAndAttributes.put("char-upcase", parameterCharacterCharacter);
        functionNameAndAttributes.put("char-upper-case?", parameterBooleanCharacter);
        functionNameAndAttributes.put("char-whitespace?", parameterBooleanCharacter);
        functionNameAndAttributes.put("char<=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char<?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char>=?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char>?", parameterBooleanCharacterINFINITE);
        functionNameAndAttributes.put("char?", parameterBooleanANY);


        // on Strings
        String[] parameterListString = {"List", "String"};
        String[] parameterStringStringANYINFINITE = {"String", "String", "ANY", "INFINITE"};
        String[] parameterStringList = {"String", "List"};
        String[] parameterStringNumberCharacter = {"String", "Number", "Character"};
        String[] parameterStringNumberString = {"String", "Number", "String"};
        String[] parameterStringCharacterINFINITE = {"String", "Character", "INFINITE"};
        String[] parameterNumberString = {"Number", "String"};
        String[] parameterSymbolString = {"Symbol", "String"};
        String[] parameterBooleanString = {"Boolean", "String"};
        String[] parameterStringStringStringINFINITE = {"String", "String", "String", "INFINITE"};
        String[] parameterBooleanStringString = {"Boolean", "String", "String"};
        String[] parameterStringString = {"String", "String"};
        String[] parameterStringStringNumber = {"String", "String", "Number"};
        String[] parameterCharacterStringNumber = {"Character", "String", "Number"};
        String[] parameterStringStringNumberNumber = {"String", "String", "Number", "Number"};
        functionNameAndAttributes.put("explode", parameterListString);
        functionNameAndAttributes.put("format", parameterStringStringANYINFINITE);
        functionNameAndAttributes.put("implode", parameterStringList);
        functionNameAndAttributes.put("int->string", parameterStringNumber);
        functionNameAndAttributes.put("list->string", parameterStringList);
        functionNameAndAttributes.put("make-string", parameterStringNumberCharacter);
        functionNameAndAttributes.put("replicate", parameterStringNumberString);
        functionNameAndAttributes.put("string", parameterStringCharacterINFINITE);
        functionNameAndAttributes.put("string->int", parameterNumberString);
        functionNameAndAttributes.put("string->list", parameterListString);
        functionNameAndAttributes.put("string->number", parameterNumberString);
        functionNameAndAttributes.put("string->symbol", parameterSymbolString);
        functionNameAndAttributes.put("string-alphabetic?", parameterBooleanString);
        functionNameAndAttributes.put("string-append", parameterStringStringStringINFINITE);
        functionNameAndAttributes.put("string-ci<=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-ci<?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-ci=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-ci>=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-ci>?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-contains-ci?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-contains?", parameterBooleanStringString);
        functionNameAndAttributes.put("string-copy", parameterStringString);
        functionNameAndAttributes.put("string-downcase", parameterStringString);
        functionNameAndAttributes.put("string-ith", parameterStringStringNumber);
        functionNameAndAttributes.put("string-length", parameterNumberString);
        functionNameAndAttributes.put("string-lower-case?", parameterBooleanString);
        functionNameAndAttributes.put("string-numeric?", parameterBooleanString);
        functionNameAndAttributes.put("string-ref", parameterCharacterStringNumber);
        functionNameAndAttributes.put("string-upcase", parameterStringString);
        functionNameAndAttributes.put("string-upper-case?", parameterBooleanString);
        functionNameAndAttributes.put("string-whitespace?", parameterBooleanString);
        functionNameAndAttributes.put("string<=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string<?", parameterBooleanStringString);
        functionNameAndAttributes.put("string=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string>=?", parameterBooleanStringString);
        functionNameAndAttributes.put("string>?", parameterBooleanStringString);
        functionNameAndAttributes.put("string?", parameterBooleanANY);
        functionNameAndAttributes.put("substring", parameterStringStringNumberNumber);       // Hier gehen entweder 1 Number oder zwei Number


        // TODO Images fehlt noch und MISC fehlt noch
    }

    public ArrayList<String> getPreDefinedFunctionsList() {
        return preDefinedFunctionsList;
    }

    public HashMap<String, String[]> getFunctionNameAndAttributes() {
        return functionNameAndAttributes;
    }

    public ArrayList<String> getPreDefinedVariablesList() {
        return preDefinedVariablesList;
    }

    public HashMap<String, String> getVariableNameAndValue() {
        return variableNameAndValue;
    }
}