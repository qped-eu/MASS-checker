package eu.qped.racket.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxCheckerTest {

    SyntaxChecker syntaxChecker = new SyntaxChecker();

    @Test
    void test() {
        assertEquals("", syntaxChecker.syntaxCheck(""));
    }

    @Test
    void bracketCheck() {
        assertEquals(0, syntaxChecker.bracketCheck("")[0]);
        assertEquals(0, syntaxChecker.bracketCheck("1")[0]);
        assertEquals(0, syntaxChecker.bracketCheck("(+ 1 (+ 1 1))")[0]);
        assertEquals(1, syntaxChecker.bracketCheck("(+ 1 (+ 1 1)")[0]);
        assertEquals(-1, syntaxChecker.bracketCheck("+ 1 (+ 1 1))")[0]);
        assertEquals(0, syntaxChecker.bracketCheck("\"(+ 1 1)\"")[0]);
        assertEquals(1, syntaxChecker.bracketCheck("(+ 1 \"1)()()()()))))(((")[0]);

        assertEquals(0, syntaxChecker.bracketCheck("[]")[1]);
        assertEquals(1, syntaxChecker.bracketCheck("[")[1]);
        assertEquals(-1, syntaxChecker.bracketCheck("]")[1]);
        assertEquals(1, syntaxChecker.bracketCheck("[[]")[1]);

        assertEquals(0, syntaxChecker.bracketCheck("([{}]) ([)")[0]);
        assertEquals(1, syntaxChecker.bracketCheck("([{}]) ([)")[1]);
        assertEquals(-1, syntaxChecker.bracketCheck("([{}]) ([})")[2]);

        assertEquals(-1, syntaxChecker.bracketCheck(")(")[0]);
        assertEquals(0, syntaxChecker.bracketCheck("(][)")[0]);
        assertEquals(-1, syntaxChecker.bracketCheck("(][)")[1]);
    }

    @Test
    void countQuotationsMarks() {
        assertFalse(syntaxChecker.countQuotationsMarks(""));
        assertFalse(syntaxChecker.countQuotationsMarks("\"test\""));
        assertTrue(syntaxChecker.countQuotationsMarks("te\"st"));
        assertTrue(syntaxChecker.countQuotationsMarks("\"test\"\""));
    }


    @Test
    void literalCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("123"));         // Number
        assertEquals("", syntaxChecker.syntaxCheck("3/2 5.5"));
        assertEquals("", syntaxChecker.syntaxCheck("#true"));       // Boolean
        assertEquals("", syntaxChecker.syntaxCheck("#t #f #false"));
        assertEquals("", syntaxChecker.syntaxCheck("'name '+"));    // Symbol
        assertEquals("", syntaxChecker.syntaxCheck("\"abcdef\""));        // String
        assertEquals("", syntaxChecker.syntaxCheck("\"This is a string\" \"This is a string with \\\" inside\""));
        assertEquals("", syntaxChecker.syntaxCheck("#\\a"));         // Character
        assertEquals("", syntaxChecker.syntaxCheck("#\\b #\\space"));
    }

    @Test
    void preDefinedVariablesCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("empty"));
        assertEquals("", syntaxChecker.syntaxCheck("false true empty e pi null"));

        assertEquals("emptyy: this variable is not defined", syntaxChecker.syntaxCheck("emptyy"));
    }

    @Test
    void onNumbersCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(* 5 3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(* 5 3 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(+ 2/3 1/16)"));
        assertEquals("", syntaxChecker.syntaxCheck("(+ 3 2 5 8)"));
        assertEquals("", syntaxChecker.syntaxCheck("(- 5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(- 5 3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(- 5 3 1)"));
        assertEquals("", syntaxChecker.syntaxCheck("(/ 12 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(/ 12 2 3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(< 42 2/5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(<= 42 2/5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(= 42 2/5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(> 42 2/5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(>= 42 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(abs -12)"));
        assertEquals("", syntaxChecker.syntaxCheck("(acos 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(add1 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(angle (make-polar 3 4))"));
        assertEquals("", syntaxChecker.syntaxCheck("(asin 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(atan 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(atan 0.5)"));
        //assertEquals("", syntaxChecker.syntaxCheck("(atan 3 4)"));  TODO 1 argument and 2 argument versions
        //assertEquals("", syntaxChecker.syntaxCheck("(atan -2 -1)"));
        assertEquals("", syntaxChecker.syntaxCheck("(ceiling 12.3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(complex? 1-2i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(conjugate 3+4i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(conjugate -2-5i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(conjugate (make-polar 3 4))"));
        assertEquals("", syntaxChecker.syntaxCheck("(cos pi)"));
        assertEquals("", syntaxChecker.syntaxCheck("(cosh 10)"));
        assertEquals("", syntaxChecker.syntaxCheck("(current-seconds)"));
        assertEquals("", syntaxChecker.syntaxCheck("(denominator 2/3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(even? 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(exact->inexact 12)"));
        assertEquals("", syntaxChecker.syntaxCheck("(exact? (sqrt 2))"));
        assertEquals("", syntaxChecker.syntaxCheck("(exp -2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(expt 16 1/2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(expt 3 -4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(floor 12.3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(gcd 6 12 8)"));
        assertEquals("", syntaxChecker.syntaxCheck("(imag-part 3+4i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(inexact->exact 12.0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(inexact? 1-2i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(integer->char 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(integer-sqrt 11)"));
        assertEquals("", syntaxChecker.syntaxCheck("(integer-sqrt -11)"));
        assertEquals("", syntaxChecker.syntaxCheck("(integer? (sqrt 2))"));
        assertEquals("", syntaxChecker.syntaxCheck("(lcm 6 12 8)"));
        assertEquals("", syntaxChecker.syntaxCheck("(log 12)"));
        assertEquals("", syntaxChecker.syntaxCheck("(magnitude (make-polar 3 4))"));
        assertEquals("", syntaxChecker.syntaxCheck("(make-polar 3 4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(make-rectangular 3 4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(max 3 2 8 7 2 9 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(min 3 2 8 7 2 9 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(modulo 9 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(modulo 3 -4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(negative? -2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(number->string 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(number->string-digits 0.9 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(number->string-digits pi 4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(exact? (sqrt 2))"));
        assertEquals("", syntaxChecker.syntaxCheck("(number? \"hello world\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(number? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(numerator 2/3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(odd? 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(positive? -2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(quotient 9 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(quotient 3 4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(random 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? 1)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? -2.349)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? #i1.23456789)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? (sqrt -1))"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? pi)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? e)"));
        assertEquals("", syntaxChecker.syntaxCheck("(rational? 1-2i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(real-part 3+4i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(real? 1-2i)"));
        assertEquals("", syntaxChecker.syntaxCheck("(remainder 9 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(remainder 3 4)"));
        assertEquals("", syntaxChecker.syntaxCheck("(round 12.3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sgn -12)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sin pi)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sinh 10)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sqr 8)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sqrt 9)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sqrt 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(sub1 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(tan pi)"));
        assertEquals("", syntaxChecker.syntaxCheck("(zero? 2)"));
    }

    @Test
    void onBooleansCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(boolean->string #false)"));
        assertEquals("", syntaxChecker.syntaxCheck("(boolean->string #true)"));
        assertEquals("", syntaxChecker.syntaxCheck("(boolean=? #true #false)"));
        assertEquals("", syntaxChecker.syntaxCheck("(boolean? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(boolean? #false)"));
        assertEquals("", syntaxChecker.syntaxCheck("(false? #false)"));
        assertEquals("", syntaxChecker.syntaxCheck("(not #false)"));
    }

    @Test
    void onSymbolsCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(symbol->string 'c)"));
        assertEquals("", syntaxChecker.syntaxCheck("(symbol=? 'a 'b)"));
        assertEquals("", syntaxChecker.syntaxCheck("(symbol? 'a)"));
    }

    @Test
    void onListsCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(append (cons 1 (cons 2 '())) (cons \"a\" (cons \"b\" empty)))"));
        assertEquals("", syntaxChecker.syntaxCheck("(assoc \"hello\" '((\"world\" 2) (\"hello\" 3) (\"good\" 0)))"));
        assertEquals("", syntaxChecker.syntaxCheck("(define a (list (list 'a 22) (list 'b 8) (list 'c 70))) a (assq 'b a)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define w (list (list (list (list \"bye\") 3) #true) 42)) w " +
                "(caaar w) (cadar w) (cdaar w) (cddar w)"));
        assertEquals("", syntaxChecker.syntaxCheck("(caadr (cons 1 (cons (cons 'a '()) (cons (cons 'd '()) '()))))"));
        assertEquals("", syntaxChecker.syntaxCheck("(define y (list (list (list 1 2 3) #false \"world\"))) y " +
                "(caar y) (cdar y)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define v (list 1 2 3 4 5 6 7 8 9 'A)) v " +
                "(cadddr v) (cdddr v) (eighth v) (fifth v) (fourth v) (seventh v) (third v) (sixth v) (list-ref v 9)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define x (list 2 \"hello\" #true)) x " +
                "(caddr x) (cadr x) (car x) (cddr x) (cdr x) (first x) (length x) (list* 4 3 x) (member \"hello\" x) (member? \"hello\" x) " +
                "(memq (list (list 1 2 3)) x) (memq? (list (list 1 2 3)) x) (memv (list (list 1 2 3)) x) (remove \"hello\" x) (remove-all \"hello\" x) " +
                "(rest x) (reverse x) (second x) (third x)"));
        assertEquals("", syntaxChecker.syntaxCheck("(cdadr (list 1 (list 2 \"a\") 3))"));
        assertEquals("", syntaxChecker.syntaxCheck("(cons 1 '())"));
        assertEquals("", syntaxChecker.syntaxCheck("(cons? (cons 1 '()))"));
        assertEquals("", syntaxChecker.syntaxCheck("(cons? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(empty? '())"));
        assertEquals("", syntaxChecker.syntaxCheck("(empty? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(list 1 2 3 4 5 6 7 8 9 0)"));
        assertEquals("", syntaxChecker.syntaxCheck("(list? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(list? '())"));
        assertEquals("", syntaxChecker.syntaxCheck("(list? (cons 1 (cons 2 '())))"));
        assertEquals("", syntaxChecker.syntaxCheck("(make-list 3 \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(null? '())"));
        assertEquals("", syntaxChecker.syntaxCheck("(null? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(range 0 10 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define hello-2 (list 2 \"hello\" #true \"hello\")) hello-2 " +
                "(remove \"hello\" hello-2) (remove-all \"hello\" hello-2)"));
    }

    @Test
    void onPosnCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(make-posn 3 3)"));
        assertEquals("", syntaxChecker.syntaxCheck("(make-posn \"hello\" #true)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define p (make-posn 2 -3)) p (posn-x p) (posn-y p)"));
        assertEquals("", syntaxChecker.syntaxCheck("(define q (make-posn \"bye\" 2)) q (posn? q)"));
        assertEquals("", syntaxChecker.syntaxCheck("(posn? 42)"));
    }

    @Test
    void onCharactersCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(char->integer #\\a)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char->integer #\\z)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-alphabetic? #\\Q)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-ci<=? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char<=? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-ci<? #\\B #\\c)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char<? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-ci=? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-ci>=? #\\b #\\C)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char>=? #\\b #\\C)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-ci>? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char>? #\\b #\\B)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-downcase #\\T)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-lower-case? #\\T)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-numeric? #\\9)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-upcase #\\t)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char-upper-case? #\\T)"));
        //assertEquals("", syntaxChecker.syntaxCheck("(char-whitespace? #\\tab)"));     TODO somehow #\tab is a HashName according to the interpreter
        assertEquals("", syntaxChecker.syntaxCheck("(char<=? #\\a #\\a #\\b)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char<? #\\a #\\b #\\c)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char=? #\\b #\\a)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char>=? #\\b #\\b #\\a)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char>? #\\A #\\z #\\a)"));
        assertEquals("", syntaxChecker.syntaxCheck("(char? \"a\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(char? #\\a)"));
    }

    @Test
    void onStringsCheck() {
        assertEquals("", syntaxChecker.syntaxCheck("(explode \"cat\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(format \"Dear Dr. ~a:\" \"Flatt\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(format \"Dear Dr. ~s:\" \"Flatt\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(format \"the value of ~s is ~a\" '(+ 1 1) (+ 1 1))"));
        assertEquals("", syntaxChecker.syntaxCheck("(implode (cons \"c\" (cons \"a\" (cons \"t\" '()))))"));
        assertEquals("", syntaxChecker.syntaxCheck("(int->string 65)"));
        assertEquals("", syntaxChecker.syntaxCheck("(list->string (cons #\\c (cons #\\a (cons #\\t '()))))"));
        assertEquals("", syntaxChecker.syntaxCheck("(make-string 3 #\\d)"));
        assertEquals("", syntaxChecker.syntaxCheck("(replicate 3 \"h\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string #\\d #\\o #\\g)"));
        assertEquals("", syntaxChecker.syntaxCheck("(string->int \"a\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string->list \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string->number \"-2.03\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string->number \"1-2i\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string->symbol \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-alphabetic? \"123\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-alphabetic? \"cat\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-append \"hello\" \" \" \"world\" \" \" \"good bye\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ci<=? \"hello\" \"WORLD\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ci<? \"hello\" \"WORLD\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ci=?  \"hello\" \"HellO\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ci>? \"WORLD\" \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ci>?  \"WORLD\" \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-contains-ci? \"At\" \"caT\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-contains? \"at\" \"cat\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-copy \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-downcase \"CAT\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-downcase \"cAt\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ith \"hello world\" 1)"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-length \"hello world\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-lower-case? \"CAT\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-numeric? \"123\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-numeric? \"1-2i\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-ref \"cat\" 2)"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-upcase \"cat\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-upcase \"cAt\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string-upper-case? \"CAT\")"));
        //assertEquals("", syntaxChecker.syntaxCheck("(string-whitespace? (string-append \" \" (string #\\tab #\\newline #\\return)))"));   TODO same #\tab problem
        assertEquals("", syntaxChecker.syntaxCheck("(string<=? \"hello\" \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string<? \"hello\" \"world\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string=? \"hello\" \"world\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string=? \"bye\" \"bye\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string>=? \"world\" \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string>? \"world\" \"hello\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string? \"hello world\")"));
        assertEquals("", syntaxChecker.syntaxCheck("(string? 42)"));
        assertEquals("", syntaxChecker.syntaxCheck("(substring \"hello world\" 1 5)"));
        assertEquals("", syntaxChecker.syntaxCheck("(substring \"hello world\" 1 8)"));
        //assertEquals("", syntaxChecker.syntaxCheck("(substring \"hello world\" 4)"));     TODO 2 argument and 3 argument versions
    }

    @Test
    void syntaxCheck() {
        assertEquals("", syntaxChecker.syntaxCheck(""));

        assertEquals("+: expects a Number, given true", syntaxChecker.syntaxCheck("(+ 1 1 1 1 1 1 1 111 1 1 1  1 true)"));

        assertEquals("+: expects a Number, given true", syntaxChecker.syntaxCheck("(+ 1 1) (+ true \"hallo\")"));

        assertEquals("+: expects a Number, given Boolean", syntaxChecker.syntaxCheck("(+ (< 1 1) 1"));
        assertEquals("+: expects a Number, given true", syntaxChecker.syntaxCheck("(+ (+ true 1) 1"));
        assertEquals("+: expects a Number, given Boolean", syntaxChecker.syntaxCheck("(+ (+ (> 1 1 1 1 1 1 1) 1) 1"));

        assertEquals("+: expects 2 argument, but found 1", syntaxChecker.syntaxCheck("(+ 1)"));

        assertEquals("", syntaxChecker.syntaxCheck("(abs 1)"));
        assertEquals("abs: expects 1 argument, but found 0", syntaxChecker.syntaxCheck("(abs )"));
        assertEquals("abs: expects 1 argument, but found 2", syntaxChecker.syntaxCheck("(abs 1 1)"));

        assertEquals("hallo: this function is not defined", syntaxChecker.syntaxCheck("(hallo 1 1)"));

        assertEquals("", syntaxChecker.syntaxCheck("1"));
        assertEquals("", syntaxChecker.syntaxCheck("\"hallo\" 1 true"));
        assertEquals("hallo: this variable is not defined", syntaxChecker.syntaxCheck("hallo"));
        assertEquals("hallo: this variable is not defined", syntaxChecker.syntaxCheck("(+ hallo 1)"));
        assertEquals("+: expects a function call, but there is no open paranthesis before this function", syntaxChecker.syntaxCheck("+ hallo 1"));

        assertEquals("", syntaxChecker.syntaxCheck("pi"));
        assertEquals("", syntaxChecker.syntaxCheck("(+ pi 1)"));
        assertEquals("boolean=?: expects a Boolean, given pi", syntaxChecker.syntaxCheck("(boolean=? pi 1)"));
        assertEquals("", syntaxChecker.syntaxCheck("empty true false pi null"));

        assertEquals("make-posn: expects 2 argument, but found 3", syntaxChecker.syntaxCheck("(make-posn 3 3 3)"));
        assertEquals("make-posn: expects 2 argument, but found 1", syntaxChecker.syntaxCheck("(make-posn \"hello\")"));
        assertEquals("posn-x: expects 1 argument, but found 2", syntaxChecker.syntaxCheck("(posn-x (make-posn 2 -3) 1)"));
        assertEquals("posn-y: expects a Name, given 1", syntaxChecker.syntaxCheck("(posn-y 1)"));

        assertEquals("append: expects a List, given true", syntaxChecker.syntaxCheck("(append (cons 1 (cons 2 '())) '() true)"));
        assertEquals("cons: expects 2 argument, but found 1", syntaxChecker.syntaxCheck("(cons 1)"));
        assertEquals("cons: expects 2 argument, but found 3", syntaxChecker.syntaxCheck("(cons 1 (cons 2 '() '()))"));
        assertEquals("", syntaxChecker.syntaxCheck("(empty? \"hallo\")"));
        assertEquals("first: expects a List, given 1", syntaxChecker.syntaxCheck("(first 1)"));
        assertEquals("length: expects a List, given true", syntaxChecker.syntaxCheck("(length true)"));
        assertEquals("ye: this variable is not defined", syntaxChecker.syntaxCheck("(list 1 \"hallo\" #\\a false pi empty null '() (cons 1 '()) (list 1 2 3) ye)"));
        assertEquals("member: expects a List, given \"peter\"", syntaxChecker.syntaxCheck("(member \"hello\" \"peter\")"));
        assertEquals("range: expects a Number, given #\\c", syntaxChecker.syntaxCheck("(range 0 #\\c 2)"));
        assertEquals("remove: expects a List, given 1", syntaxChecker.syntaxCheck("(remove \"hello\" 1)"));
        assertEquals("remove: expects 2 argument, but found 3", syntaxChecker.syntaxCheck("(remove \"hello\" smh (list \"hello\" #true \"hello\"))"));

        assertEquals("", syntaxChecker.syntaxCheck("(if true true false)"));
        assertEquals("", syntaxChecker.syntaxCheck("(if (< 2 1) pi  (list 1 2 3))"));
        assertEquals("if: expects a Boolean, given Number", syntaxChecker.syntaxCheck("(if (+ 2 1) pi  (list 1 2 3))"));

        assertEquals("", syntaxChecker.syntaxCheck("(cond [true 1])"));
        assertEquals("", syntaxChecker.syntaxCheck("(cond [true 1] [false 2] [else true])"));
        assertEquals("cond: found an else clause that isn't the last clause in its cond expression", syntaxChecker.syntaxCheck("(cond [true 1] [else true] [false 2])"));
        assertEquals("cond: expected a clause with a question and an answer, but found Number", syntaxChecker.syntaxCheck("(cond [true 1] 1 [false 2])"));
        assertEquals("cond: expected a clause with a question and an answer, but found a clause with 3 parts", syntaxChecker.syntaxCheck("(cond [true 1] [false 2] [else true \"hallo\"])"));
        assertEquals("<: expects a Number, given true", syntaxChecker.syntaxCheck("(cond [true 1] [false 2] [else (< 1 true)])"));
        assertEquals("+: expects a Number, given #\\c", syntaxChecker.syntaxCheck("(cond [true 1] [false (+ 1 #\\c)] [else (< 1 2)])"));
        assertEquals("<: expects a Number, given List", syntaxChecker.syntaxCheck("(cond [(< '() 1) 1] [false (+ 1 #\\c)] [else (< 1 2)])"));

    }
}