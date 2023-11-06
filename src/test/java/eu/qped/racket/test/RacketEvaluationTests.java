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

        s = "(* )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(- )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(+ )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(/ )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(< )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(> )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(<= )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(>= )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(abs )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(abs 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(add1 )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(add1 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(ceiling )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(ceiling 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(even? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(even? 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(exp )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(exp 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(floor )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(floor 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(log 8 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log 32 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(5), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(/ (log 8) (log 2))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(log )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(log 2 3 4)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 3", thrown.getMessage());
    }

    @Test
    void testEqual() throws Exception {
        String s = "(= 1 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(= 1 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= 1 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= true true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Number/expects a float", thrown.getMessage());

        s = "(= \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Number/expects a float", thrown.getMessage());

        s = "(= 3 3 3 3 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(= 3 4 3 3 4 3 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(= 3 4 3 3 \"hello\" true 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Number/expects a float", thrown.getMessage());

        s = "(= )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(max )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(min )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects atleast 1 argument, but found 0", thrown.getMessage());
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

        s = "(modulo 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 1", thrown.getMessage());

        s = "(modulo 3 2 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 3", thrown.getMessage());
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

        s = "(negative? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(negative? 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(odd? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(odd? 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(positive? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(positive? 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
    }

    @Test
    void testRandom() throws Exception {
        String s = "(random 1)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Integer.toString(0), inter.evaluateExpressions());

        s = "(random 5 10 2 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects between 0 and 2 arguments, but found 4", thrown.getMessage());
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

        s = "(round )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(round 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(sqr )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(sqr 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(sqrt )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(sqrt 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(sub1 )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(sub1 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(zero? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(zero? 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(boolean=? true true true true true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 5", thrown.getMessage());

        s = "(boolean=? 123 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Boolean/expects a boolean", thrown.getMessage());

        s = "(boolean=? 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 1", thrown.getMessage());
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

        s = "(boolean? true true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(false? true true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());

        s = "(false? 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Boolean/expects a boolean", thrown.getMessage());
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

        s = "(not true true)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());

        s = "(not 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of Boolean/expects a boolean", thrown.getMessage());
    }

    @Test
    void testCustomFunction() throws Exception {
        String s = "(define (double x)\n" +
                "(abs x))\n" +
                "(double -21)";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(21), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(add1 x))\n" +
                "(double -21)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-20), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(ceiling x))\n" +
                "(double 1.8)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(/ x 0.25))\n" +
                "(double 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(= x 1))\n" +
                "(double 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(exp x))\n" +
                "(double 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(define (double x)\n" +
                "(floor x))\n" +
                "(double -1.1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-2), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(>= x 0))\n" +
                "(double 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(> x 1))\n" +
                "(double 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(<= x 1))\n" +
                "(double 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(< x 1))\n" +
                "(double 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(log x))\n" +
                "(double 0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1), Float.toString(Math.round(Float.valueOf(inter.evaluateExpressions()))));

        s = "(define (double x)\n" +
                "(max 1 x 3))\n" +
                "(double 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(min -1 x -3))\n" +
                "(double -6)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-6), inter.evaluateExpressions());

        s = "(define (double x)\n" +
                "(- 1 (- 2 1) x (+ 0 5))\n" +
                "(double 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-6), inter.evaluateExpressions());

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

        s = "(define (calculate x)\n" +
                "(sqr x))\n" +
                "(calculate 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(4), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(sqrt x))\n" +
                "(calculate 9)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(3), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(sub1 x))\n" +
                "(calculate -0.5)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(-1.5f), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(zero? x))\n" +
                "(calculate 109)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(modulo x 7))\n" +
                "(calculate 23)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(2), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(negative? x))\n" +
                "(calculate 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(odd? x))\n" +
                "(calculate 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(positive? x))\n" +
                "(calculate 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(random x))\n" +
                "(calculate 1)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Integer.toString(0), inter.evaluateExpressions());

        s = "(define (calculate x)\n" +
                "(round x))\n" +
                "(calculate 0.2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(0), inter.evaluateExpressions());

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

        s = "(append (cons 1 empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("(cons 1.0 '())", inter.evaluateExpressions());

        s = "(append )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("'()", inter.evaluateExpressions());

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

        s = "(empty? )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(empty? empty empty)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(first (cons #f 1.0))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(first (list #f 1.0))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(first (cons \"hello\" empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello\"", inter.evaluateExpressions());

        s = "(first )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(first (cons #f 1.0) (cons #f 1.0))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(length )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(length (cons #f 1.0) (cons #f 1.0))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(member \"hello\" (cons \"hello\" empty))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(member 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 1", thrown.getMessage());

        s = "(member 2 (list 1 2 3) (list 2 3 4))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 3", thrown.getMessage());
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

        s = "(range )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects between 1 and 3 arguments, but found 0", thrown.getMessage());

        s = "(range 5 10 2 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects between 1 and 3 arguments, but found 4", thrown.getMessage());
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

        s = "(remove )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 0", thrown.getMessage());

        s = "(remove 2 (list 1 2) (list 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 3", thrown.getMessage());
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

        s = "(remove-all )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 0", thrown.getMessage());

        s = "(remove-all 2 (list 1 2) (list 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 arguments, but found 3", thrown.getMessage());
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

        s = "(rest )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(rest 2 (list 1 2) (list 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 3", thrown.getMessage());
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

        s = "(reverse )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(reverse (list 1 2) (list 2 3))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());

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

        s = "(second (cons 1 (cons \"hello\" (cons 3 empty))))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello\"", inter.evaluateExpressions());

        s = "(second )";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 0", thrown.getMessage());

        s = "(second (cons #f 1.0) (cons #f 1.0))";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());
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

        s = "(make-list 2)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 1", thrown.getMessage());

        s = "(make-list 2 2 3)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 3", thrown.getMessage());
    }

    @Test
    void testStringEQ() throws Exception {
        //String rktFile = IOUtils.toString(ClassLoader.getSystemResourceAsStream("Demo.rkt"), Charset.defaultCharset());

        String s = "(string=? \"hello\" \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string=? \"hellow\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string=? \"hello\" \"hellow\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string=? \"Hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string=? \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string=? \"hello\" \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string=? \"hello\" \"hello\" \"hellow\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

//        s = "(string=? hello \"hello\")";
//        inter = new DrRacketInterpreter(s);
//        inter.evaluate();
//        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string=? hello \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringQ() throws Exception {
        String s = "(string? \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string? hello)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string? \"HellO\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string? \"HellO 2345 7 \")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string? \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 2", thrown.getMessage());

        s = "(string? \"hello\" \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 5", thrown.getMessage());
    }

    @Test
    void testStringAppend() throws Exception {
        String s = "(string-append \"hello\" \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hellohello\"", inter.evaluateExpressions());

        s = "(string-append \"hello\" \" hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello hello\"", inter.evaluateExpressions());

        s = "(string-append \"hello\" \" hello\" \"123hello\" \"123hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello hello123hello123hello\"", inter.evaluateExpressions());

        s = "(string-append \"hello\" 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-append \"hello\" 123  \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringContains() throws Exception {
        String s = "(string-contains? \"hello\" \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-contains? \"ll\" \" hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-contains? \"lwl\" \" hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-contains? \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 4", thrown.getMessage());

        s = "(string-contains? \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 2 argument, but found 1", thrown.getMessage());

        s = "(string-contains? ll \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringDowncase() throws Exception {
        String s = "(string-downcase \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello\"", inter.evaluateExpressions());

        s = "(string-downcase \"Hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"hello\"", inter.evaluateExpressions());

        s = "(string-downcase \"HeLlOß?123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"helloß?123\"", inter.evaluateExpressions());

        s = "(string-downcase \"HeLlOß?123 123aSd\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"helloß?123 123asd\"", inter.evaluateExpressions());

        s = "(string-downcase \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 4", thrown.getMessage());

        s = "(string-downcase 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-downcase ll)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringUpcase() throws Exception {
        String s = "(string-upcase \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"HELLO\"", inter.evaluateExpressions());

        s = "(string-upcase \"Hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"HELLO\"", inter.evaluateExpressions());

        s = "(string-upcase \"HeLlOß?123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"HELLOSS?123\"", inter.evaluateExpressions());

        s = "(string-upcase \"HeLlOß?123 123aSd\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals("\"HELLOSS?123 123ASD\"", inter.evaluateExpressions());

        s = "(string-upcase \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 4", thrown.getMessage());

        s = "(string-upcase 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-upcase ll)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringLowerCaseQ() throws Exception {
        String s = "(string-lower-case? \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-lower-case? \"Hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-lower-case? \"HeLlOß?123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-lower-case? \"helloß?123 123asd\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-lower-case? \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 4", thrown.getMessage());

        s = "(string-lower-case? 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-lower-case? ll)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringUpperCaseQ() throws Exception {
        String s = "(string-upper-case? \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-upper-case? \"HellO\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-upper-case? \"HELLO\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-upper-case? \"HeLlOß?123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-upper-case? \"helloß?123 123asd\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(false), inter.evaluateExpressions());

        s = "(string-upper-case? \"HELLOSS?123 123ASD\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Boolean.toString(true), inter.evaluateExpressions());

        s = "(string-upper-case? \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 4", thrown.getMessage());

        s = "(string-upper-case? 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-upper-case? ll)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

    @Test
    void testStringLength() throws Exception {
        String s = "(string-length \"hello\")";
        DrRacketInterpreter inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(5), inter.evaluateExpressions());

        s = "(string-length \"HellO123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(8), inter.evaluateExpressions());

        s = "(string-length \"HELLO\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(5), inter.evaluateExpressions());

        s = "(string-length \"HeLlOß?123\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(10), inter.evaluateExpressions());

        s = "(string-length \"helloß?123 123asd\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(17), inter.evaluateExpressions());

        s = "(string-length \"HELLOSS?123 123ASD\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        assertEquals(Float.toString(18), inter.evaluateExpressions());

        s = "(string-length \"hello\" \"hello\" \"hello\" \"hello\")";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        DrRacketInterpreter finalInter = inter;
        Exception thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("expects only 1 argument, but found 4", thrown.getMessage());

        s = "(string-length 123)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());

        s = "(string-length ll)";
        inter = new DrRacketInterpreter(s);
        inter.evaluate();
        finalInter = inter;
        thrown = assertThrows(Exception.class, finalInter::evaluateExpressions);
        Assertions.assertEquals("Expression isnt instance of String/expects a String", thrown.getMessage());
    }

//    @Test
//    void testDeklarativeProgrammierung() throws Exception {
//        String s = "(define (lst-append l1 l2)\n" +
//                "(cond\n" +
//                "[(empty? l1) l2]\n" +
//                "[else (cons (first l1) (lst-append (rest l1) l2))]))\n" +
//                "(define (lst-reverse l)\n" +
//                "(cond\n" +
//                "[(empty? l) empty]\n" +
//                "[else (lst-append (lst-reverse (rest l)) (list (first l)))]))";
//        DrRacketInterpreter inter = new DrRacketInterpreter(s);
//        inter.evaluate();
//    }
}
