package eu.qped.framework;

import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.utils.SupportedLanguages;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Translator {

    public String translate(String langFrom, String langTo, String text) throws TranslationException {
        String result = "";
        try {
            // INSERT YOU URL HERE
            String urlStr = "https://script.google.com/macros/s/AKfycbyYDebha3rU7OthP0hCULlw8Mz_-7m7ol5jsS57kK8EO9lCNtoMfc5x5VXaS6weviFgqw/exec" +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + langTo +
                    "&source=" + langFrom;
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            if (con.getResponseCode() == -1) {
                throw new TranslationException(langFrom, langTo);
            }

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (response.toString().contains("<!DOCTYPE")) {
                result = text;
            } else {
                result = response.toString();
            }
        } catch (IOException e) {
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
        }

        return result;
    }

    public void translateBody(String pref, Feedback feedback) {
        try {
            feedback.setBody(translate("en", pref, feedback.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            feedback.setBody(translate("en", pref, feedback.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void translateFeedback(String pref, eu.qped.framework.feedback.Feedback feedback) {
        try {
            feedback.setReadableCause(translate(SupportedLanguages.ENGLISH, pref, feedback.getReadableCause()));
            List<Hint> feedbackHints = new ArrayList<>();
            for (Hint hint : Optional.ofNullable(feedback.getHints()).orElse(new ArrayList<>())) {
                if (hint.getType().equals(HintType.TEXT)) {
                    feedbackHints.add(Hint.builder()
                            .type(hint.getType())
                            .content(translate(SupportedLanguages.ENGLISH, pref, hint.getContent()))
                            .build());

                } else {
                    feedbackHints.add(Hint.builder()
                            .type(hint.getType())
                            .content(hint.getContent())
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void translateMetricsBody(String pref, MetricsFeedback feedback) {

        try {
            feedback.setBody(translate("en", pref, feedback.getBody()));
            String[] words;
            words = feedback.getBody().split("[.]");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                result.append(word).append("\n\n");
            }
            feedback.setBody(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}