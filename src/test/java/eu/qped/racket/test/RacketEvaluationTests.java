package eu.qped.racket.test;

import eu.qped.racket.interpret.DrRacketInterpreter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osgi.service.application.ApplicationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RacketEvaluationTests {

    @Test
    void testMultiplication() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(* 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(* 0 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(* 1 (* 2 1))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(* 1 (* 2 1) 1 (* 0 5) 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(* (* 2 2) (* 1 (* 3 2)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(24), inter.evaluateExpressions());

        s = "(* 0.5 0.25)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString((float)0.125), inter.evaluateExpressions());

        s = "(* -2 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(* -2 -1 -4)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-8), inter.evaluateExpressions());
    }

    @Test
    void testMinus() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(- 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(- 2 (- 2 1))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(- 1 (- 2 1) 1 (- 0 5))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());

        s = "(- (- 2 2) (- 1 (- 3 2)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());
    }

    @Test
    void testPlus() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(+ 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(+ 1 (+ 2 1))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());

        s = "(+ 1 (+ 2 1) 1 (+ 0 5))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(10), inter.evaluateExpressions());

        s = "(+ (+ 2 2) (+ 1 (+ 3 2)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(10), inter.evaluateExpressions());

        s = "(+ 0.5 0.25)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString((float)0.75), inter.evaluateExpressions());

        s = "(+ -2 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-3), inter.evaluateExpressions());
    }

    @Test
    void testDivision() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(/ 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(/ 0 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(/ 1 (/ 2 1))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString((float)0.5), inter.evaluateExpressions());

        s = "(/ 1 (/ 2 1) 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString((float)0.5), inter.evaluateExpressions());

        s = "(/ 0.5 0.25)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(/ -2 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(/ -2 -1 -4)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString((float)-0.5), inter.evaluateExpressions());
    }

    @Test
    void testMultipleExpressions() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(+ 1 1)";
        List answer = Arrays.stream("2".split("[ ]"))
                .map(x -> Float.valueOf(x))
                .collect(Collectors.toList());
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(answer, inter.getAllExpressionEvaluations());

        s = "(+ 1 1) (+ 1 1)";
        answer = Arrays.stream("2 2".split("[ ]"))
                .map(x -> Float.valueOf(x))
                .collect(Collectors.toList());
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(answer, inter.getAllExpressionEvaluations());

        s = "(+ 1 1) (+ 1 1) \n(+ (+ 2 2) (+ 1 (+ 3 2)))";
        answer = Arrays.stream("2 2 10".split("[ ]"))
                .map(x -> Float.valueOf(x))
                .collect(Collectors.toList());
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(answer, inter.getAllExpressionEvaluations());
    }

    @Test
    void testLessThan() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(< 1 20)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(< 2 3 4 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(< 1  1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(< 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(< 2 3 6 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(< 2 3 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(< -2 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testGreaterThan() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(> 1 20)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(> 2 3 4 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(> 1  1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(> 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(> 30 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(> 0 -3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(> 20 -1 -3 -10)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void equal() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(= 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(= 1 1 1 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(= 1  2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= 1  2 1 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= 2 2 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= -1 -1 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testLessOrEqualThan() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(<= 1 20)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(<= 2 2 4 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(<= 1  1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(<= 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(<= 2 3 6 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(<= 2 3 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(<= -2 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testGreaterOrEqualThan() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(>= 1 20)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(>= 2 2 4 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(>= 1  1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(>= 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(>= 30 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(>= 0 -3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(>= 20 -1 -3 -10)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testAbsolute() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(abs 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(abs -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(abs 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0.5f), inter.evaluateExpressions());

        s = "(abs -0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0.5f), inter.evaluateExpressions());

        s = "(abs 4 7 8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());
    }

    @Test
    void testAdd1() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(add1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(add1 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(add1 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1.5f), inter.evaluateExpressions());

        s = "(add1 -0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0.5f), inter.evaluateExpressions());

        s = "(add1 4 7 8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(5), inter.evaluateExpressions());
    }

    @Test
    void testCeiling() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(ceiling 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(ceiling 1.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(ceiling 1.8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(ceiling -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(ceiling -1.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(ceiling 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(ceiling -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(ceiling -0.01)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(ceiling 4.8 3 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(5), inter.evaluateExpressions());
    }

    @Test
    void testEven() throws Exception {
        String s = "(even? 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(even? 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(even? 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(even? 9)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(even? -9)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(even? -2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(even? 9 10 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testExp() throws Exception {
        String s = "(exp 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(exp 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(7), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(exp 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(exp 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(exp 2 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(7), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));
    }

    @Test
    void testFloor() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(floor 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(floor 1.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(floor 1.8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(floor -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(floor -1.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-2), inter.evaluateExpressions());

        s = "(floor 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(floor -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(floor -0.01)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(floor 4.8 3 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());
    }

    @Test
    void testLog() throws Exception {
        String s = "(log 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log 2 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));
    }

    @Test
    void testMax() throws Exception {
        String s = "(max 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(max 1 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());

        s = "(max -1 -6 -3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(max 10 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(10), inter.evaluateExpressions());

        s = "(max 10 20 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(20), inter.evaluateExpressions());
    }

    @Test
    void testMin() throws Exception {
        String s = "(min 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(min 1 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(min -1 -6 -3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-6), inter.evaluateExpressions());

        s = "(min 10 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(min 10 1 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());
    }

    @Test
    void testModulo() throws Exception {
        String s = "(modulo 0 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(modulo 1 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(modulo 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(modulo 10 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(modulo 23 7)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(modulo -4 -2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(modulo -4 -3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), inter.evaluateExpressions());

        s = "(modulo -4 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());
    }

    @Test
    void testNegative() throws Exception {
        String s = "(negative? 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(negative? -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(negative? -20)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(negative? 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(negative? -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(negative? 9 10 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testOdd() throws Exception {
        String s = "(odd? 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(odd? -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(odd? -20)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(odd? 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(odd? -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(odd? 9 10 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testPositive() throws Exception {
        String s = "(positive? 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(positive? -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(positive? -20)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(positive? 0)";    //Like Racket, but unintuitive
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(positive? -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(positive? 9 10 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testRandom() throws Exception {
        String s = "(random 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Integer.toString(0), inter.evaluateExpressions());
    }

    @Test
    void testRound() throws Exception {
        String s = "(round 0)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(round 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(round 0.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(round 0.9)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(round 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(round 0.51)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(round 0.49)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(round 1.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(round 3.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());

        s = "(round 4.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());
    }

    @Test
    void testSqr() throws Exception {
        String s = "(sqr 0)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(sqr 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(sqr 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());

        s = "(sqr 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0.25f), inter.evaluateExpressions());

        s = "(sqr 4)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(16), inter.evaluateExpressions());

        s = "(sqr 8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(64), inter.evaluateExpressions());

        s = "(sqr -8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(64), inter.evaluateExpressions());
    }

    @Test
    void testSqrt() throws Exception {
        String s = "(sqrt 0)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(sqrt 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(sqrt 9)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());
    }

    @Test
    void testSub1() throws Exception {
        String s = "(sub1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

        s = "(sub1 -1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-2), inter.evaluateExpressions());

        s = "(sub1 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-0.5f), inter.evaluateExpressions());

        s = "(sub1 -0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1.5f), inter.evaluateExpressions());

        s = "(sub1 4 7 8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());
    }

    @Test
    void testZero() throws Exception {
        String s = "(zero? 0)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(zero? 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(zero? -0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(zero? 109)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testBooleanEQ() throws Exception { //"EQ" => "=?"
        String s = "(boolean=? true true)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean=? true false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? false true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? false false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean=? #true #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean=? #true #false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? #false #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? #false #false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean=? #t #t)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean=? #t #f)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? #f #t)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean=? #f #f)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testBooleanQ() throws Exception { //"Q" => "?"
        String s = "(boolean? true)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? 100)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(boolean? #f)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? #t)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? #false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(boolean? \"a b\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testFalseQ() throws Exception { //"Q" => "?"
        String s = "(false? true)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(false? false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(false? #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(false? #false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(false? #f)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(false? #t)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testNot() throws Exception {
        String s = "(not true)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(not false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(not #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(not #false)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(not #f)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(not #t)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testCustomFunction() throws Exception {
        String s = "(define (double x)\n" +
                "(* x 2))\n" +
                "(double 21)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(42), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(* x 2))\n" +
                "(define (triple x)\n"+
                "(* x 3))\n" +
                "(double 21)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(42), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(* x 2))\n" +
                "(define (triple x)\n"+
                "(* x 3))\n" +
                "(triple 21)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(63), inter.evaluateExpressions());

    }

    @Test
    void testCons() throws Exception {
        String s = "(cons 1 empty)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 '())", inter.evaluateExpressions());

        s = "(cons 1 (cons 2 (cons 3 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 '())))", inter.evaluateExpressions());

        s = "(cons 1 (cons 2 (cons 3 '())))";   //TODO Accept '() and Quotes in general
        inter = new DrRacketInterpreter(s);
        //inter.master();
        //assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 '())))", inter.evaluateExpressions());
    }

    @Test
    void testAppend() throws Exception {
        String s = "(append (cons 1 empty) (cons 2 empty))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 '()))", inter.evaluateExpressions());

        s = "(append (cons 1 (cons 2 (cons 3 empty))) (cons 1 empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons 1.0 '()))))", inter.evaluateExpressions());

        s = "(append (cons 1 (cons 2 (cons 3 empty))) (list 1))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons 1.0 '()))))", inter.evaluateExpressions());

        s = "(append (cons 1 (cons 2 (cons 3 empty))) (list #true))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons true '()))))", inter.evaluateExpressions());

    }

    @Test
    void testEmptyQ() throws Exception {
        String s = "(empty? (cons 1 empty))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(empty? empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(empty? (cons 1 (cons 2 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());
    }

    @Test
    void testFirst() throws Exception {
        String s = "(first (cons 1 (cons 2 empty)))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(first (cons (+ 1 1) (cons 3 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(first (cons 1 empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(first (cons true empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(first (cons #f empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(first (cons hello empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("hello", inter.evaluateExpressions());
    }

    @Test
    void testLength() throws Exception {
        String s = "(length (cons 1 (cons 2 empty)))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(length (cons (+ 1 1) (cons 3 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(length (cons 1 empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(1), inter.evaluateExpressions());

        s = "(length empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

//        s = "(length '())";
//        inter = new DrRacketInterpreter(s);
//        inter.evaluate();
//        assertEquals(Float.toString(0), inter.evaluateExpressions());     //TODO '() NullPointer in goDeeper etc

        s = "(length (cons 1 (cons 2 (cons 3 (cons 4 empty)))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());
    }

    @Test
    void testList() throws Exception {
        String s = "(list 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 '())", inter.evaluateExpressions());

        s = "(list 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 '()))", inter.evaluateExpressions());

        s = "(list 1 #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons true '()))", inter.evaluateExpressions());

        s = "(list 1 2 3 4)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons 4.0 '()))))", inter.evaluateExpressions());

        s = "(list (+ 1 3) 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 4.0 (cons 2.0 '()))", inter.evaluateExpressions());

        s = "(list empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons '() '())", inter.evaluateExpressions());
    }

    @Test
    void testMember() throws Exception {
        String s = "(member 1 (list 1 2 3))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(member 4 (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(member (+ 1 1) (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(member 1 (cons 1 empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testMemberQ() throws Exception {
        String s = "(member? 1 (list 1 2 3))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(member? 4 (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(member? (+ 1 1) (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(member? 1 (cons 1 empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());
    }

    @Test
    void testRange() throws Exception {
        String s = "(range 0 10 2)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 (cons 2.0 (cons 4.0 (cons 6.0 (cons 8.0 '())))))", inter.evaluateExpressions());

        s = "(range 0 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 (cons 1.0 (cons 2.0 (cons 3.0 (cons 4.0 '())))))", inter.evaluateExpressions());

        s = "(range 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 (cons 1.0 (cons 2.0 (cons 3.0 (cons 4.0 '())))))", inter.evaluateExpressions());

        s = "(range #true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression(Start/End) isnt Instance of Number", thrown.getMessage());
        System.out.println("Exceptiontest ?????????????????????????????????????????????????????????????");

        s = "(range 0 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 '())", inter.evaluateExpressions());

        s = "(range 0 1 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 '())", inter.evaluateExpressions());

        s = "(range 0 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 0.0 (cons 1.0 '()))", inter.evaluateExpressions());

        s = "(range 0 0 0)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("'()", inter.evaluateExpressions());

        s = "(range 5 10 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 5.0 (cons 7.0 (cons 9.0 '())))", inter.evaluateExpressions());
    }

    @Test
    void testRemove() throws Exception {
        String s = "(remove 2 (list 1 2 3 2))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 3.0 (cons 2.0 '())))", inter.evaluateExpressions());

        s = "(remove 1 (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 3.0 '()))", inter.evaluateExpressions());

        s = "(remove 1 (list 1 (+ 1 1) 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 3.0 '()))", inter.evaluateExpressions());

        s = "(remove 2 (list 1 4 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 4.0 (cons 3.0 '())))", inter.evaluateExpressions());

        s = "(remove 2 (list 1 4 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 4.0 (cons 3.0 '())))", inter.evaluateExpressions());

        s = "(remove (list 1 2) (list 1 2 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons 2.0 '()))))", inter.evaluateExpressions());

        s = "(remove (+ 1 1) (list 1 2 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 3.0 (cons 2.0 '())))", inter.evaluateExpressions());
    }
    @Test
    void testRemoveAll() throws Exception {
        String s = "(remove-all 2 (list 1 2 3 2))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 3.0 '()))", inter.evaluateExpressions());

        s = "(remove-all 1 (list 1 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 3.0 '()))", inter.evaluateExpressions());

        s = "(remove-all 2 (list 1 4 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 4.0 (cons 3.0 '())))", inter.evaluateExpressions());

        s = "(remove-all 2 (list 1 4 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 4.0 (cons 3.0 '())))", inter.evaluateExpressions());

        s = "(remove-all (list 1 2) (list 1 2 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 2.0 (cons 3.0 (cons 2.0 '()))))", inter.evaluateExpressions());

        s = "(remove-all (+ 1 1) (list 1 2 3 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 3.0 '()))", inter.evaluateExpressions());
    }

    @Test
    void testRest() throws Exception {
        String s = "(rest (cons 1 empty))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("'()", inter.evaluateExpressions());

        s = "(rest (cons 1 (cons 2 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 '())", inter.evaluateExpressions());

        s = "(rest (list 1 2 3 4))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 3.0 (cons 4.0 '())))", inter.evaluateExpressions());
    }

    @Test
    void testReverse() throws Exception {
        String s = "((reverse (cons 1 empty)))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 '())", inter.evaluateExpressions());

        s = "(reverse (cons 1 (cons 2 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 1.0 '()))", inter.evaluateExpressions());

        s = "(reverse (list 1 2 3 4))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 4.0 (cons 3.0 (cons 2.0 (cons 1.0 '()))))", inter.evaluateExpressions());

        s = "(reverse empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("'()", inter.evaluateExpressions());

//        s = "(reverse '())";
//        inter = new DrRacketInterpreter(s);
//        inter.evaluate();
//        assertEquals("'()", inter.evaluateExpressions());             //TODO NullPointer in goDeeper etc
    }

    @Test
    void testSecond() throws Exception {
        String s = "(second (cons 1 (cons 2 empty)))";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(second (cons (+ 1 1) (cons 3 empty)))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());

        s = "(second (cons 1 (cons 2 (cons 3 empty))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(second (cons 1 (cons true (cons 3 empty))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(second (cons 1 (cons #f (cons 3 empty))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(second (cons 1 (cons hello (cons 3 empty))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("hello", inter.evaluateExpressions());
    }

    @Test
    void testMakeList() throws Exception {
        String s = "(make-list 2 2)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 2.0 (cons 2.0 '()))", inter.evaluateExpressions());

        s = "(make-list 0 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("'()", inter.evaluateExpressions());

        s = "(make-list 1 5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 5.0 '())", inter.evaluateExpressions());

        s = "(make-list 3 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 (cons 1.0 (cons 1.0 '())))", inter.evaluateExpressions());
    }
}
