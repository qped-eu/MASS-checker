package eu.qped.racket.buildingBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PreDefinedFunctionsAndVariables {

    private String[] preDefinedFunctions = new String[]{
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
            "char->integer", "char-alphabetic?", "char-ci<=?", "char-ci<?", "char-ci=?", "char-ci>=?", "char-ci>?" ,"char-downcase", "char-lower-case?", "char-numeric?", "char-upcase",
            "char-upper-case?", "char-whitespace?", "char<=?", "char<?", "char=?", "char>=?", "char>?", "char?",
            // on Strings
            "explode", "format", "implode","int->string","list->string","make-string","replicate","string","string->int","string->list","string->number","string->symbol","string-alphabetic?",
            "string-append","string-ci<=?","string-ci<?","string-ci=?","string-ci>=?","string-ci>?","string-contains-ci?","string-contains?","string-copy","string-downcase","string-ith",
            "string-length","string-lower-case?","string-numeric?","string-ref","string-upcase","string-upper-case?","string-whitespace?","string<=?","string<?","string=?","string>=?",
            "string>?","string?","substring",
            // special
            "cond"};

    /** Alle definierten Variabeln, sowohl vordefiniert, als auch selber definierte */
    private String[] preDefinedVariables = new String[]{"empty", "pi", "null", "e"};
    private ArrayList<String> preDefinedFunctionsList;
    private ArrayList<String> preDefinedVariablesList;
    private HashMap<String, String[]> functionNameAndAttributes;
    private HashMap<String, String> variableNameAndValue;


    public PreDefinedFunctionsAndVariables() {
        preDefinedFunctionsList = new ArrayList<>(Arrays.asList(preDefinedFunctions));
        preDefinedVariablesList = new ArrayList<>(Arrays.asList(preDefinedVariables));
    }

    public ArrayList<String> getPreDefinedFunctionsList() {
        return preDefinedFunctionsList;
    }

    public ArrayList<String> getPreDefinedVariablesList() {
        return preDefinedVariablesList;
    }

    public HashMap<String, String> getVariableNameAndValue() {
        return variableNameAndValue;
    }
}
