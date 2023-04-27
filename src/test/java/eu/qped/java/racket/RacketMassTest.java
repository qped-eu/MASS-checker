package eu.qped.java.racket;

import com.google.gson.JsonParser;
import eu.qped.framework.qf.QFQuestion;
import eu.qped.framework.qf.QfObject;
import eu.qped.racket.RacketChecker;
import eu.qped.racket.interpret.DrRacketInterpreter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RacketMassTest {

    @Test
    void testMass() throws Exception {
        JSONParser parser = new JSONParser();
        String answer = "";
        String questionSolution = "";
        List expectedFeedback = new LinkedList();

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/system-tests/racket/qf-input.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            answer = (String) jsonObject.get("answer");
            System.out.println(answer);

            questionSolution = ((JSONObject)jsonObject.get("question")).get("solution").toString();
            System.out.println(questionSolution);

            Object expected = parser.parse(new FileReader("src/test/resources/system-tests/racket/qf-expected.json"));

            JSONObject jsonExpected =  (JSONObject) expected;

            expectedFeedback = (List) jsonExpected.get("feedback");
            System.out.println(expectedFeedback);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        QfObject qfObject = new QfObject();
        qfObject.setAnswer(answer);
        QFQuestion qfq = new QFQuestion();
        qfq.setSolution(questionSolution);
        qfObject.setQuestion(qfq);

        RacketChecker racketChecker = new RacketChecker();
        racketChecker.check(qfObject);

        List<String> actualFeedback = Arrays.stream(qfObject.getFeedback()).collect(Collectors.toList());
        Arrays.stream(qfObject.getFeedback()).forEach(x -> System.out.println(x));

        System.out.println("WWWWWWWWWWWWWWWWW");
        expectedFeedback.forEach(y -> System.out.println(y));
        System.out.println("WWWWWWWWWWWWWWWWW");
        actualFeedback.forEach(y -> System.out.println(y));

        assertEquals(expectedFeedback.size(), actualFeedback.size());

        for (int i = 0; i < expectedFeedback.size(); i++) {
            assertEquals(expectedFeedback.get(i), actualFeedback.get(i));
            System.out.println(i);
        }

    }
}
