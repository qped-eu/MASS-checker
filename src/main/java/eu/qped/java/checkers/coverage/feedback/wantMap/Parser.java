package eu.qped.java.checkers.coverage.feedback.wantMap;

import eu.qped.java.checkers.coverage.feedback.DefaultFB;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private class CustomParser {
        private final int CLASS_NAME = 1, IDENTIFIER = 2, FB = 3;
        private final Pattern CUSTOM = Pattern.compile("^([a-zA-Z0-9]+):([a-zA-Z0-9]+):(.*)$");

        private Matcher matcher;
        private boolean locked = false;

        boolean find(String str) {
            locked = false;
            if (Objects.nonNull(str)) {
                matcher = CUSTOM.matcher(str);
                locked = matcher.find();
            }
            return locked;
        }

        String classname() {
            if (locked)
                return matcher.group(CLASS_NAME);
            return null;
        }

        String identifier() {
            if (locked)
                return matcher.group(IDENTIFIER);
            return null;
        }

        String feedback() {
            if (locked) return matcher.group(FB);
            return null;
        }
    }
    private class DefaultParser {
        private final int KEY = 1;
        private final Pattern TEST = Pattern.compile("^([a-zA-Z0-9]*):TEST$");

        private Matcher matcher;
        private boolean locked = false;

        boolean find(String str) {
            locked = false;
            if (Objects.nonNull(str)) {
                matcher = TEST.matcher(str);
                locked = matcher.find();
            }
            return locked;
        }

        String key() {
            if (locked)
                return matcher.group(KEY);
            return null;
        }

    }

    private final DefaultParser defaultParser = new DefaultParser();
    private final CustomParser customParser = new CustomParser();

    public Provider parse(String language, List<String> wfs) {
        Map<String, String> wantedFeedback = new HashMap<>();
        Set<String> testFeedback = new HashSet<>();
        boolean testFeedbackForAll = false;

        for (String wf : wfs) {
            if (customParser.find(wf)) {
                wantedFeedback.put(customParser.classname()+customParser.identifier(), customParser.feedback());
            } else if (defaultParser.find(wf)) {
                if (defaultParser.key().isBlank()) {
                    testFeedbackForAll = true;
                } else {
                    testFeedback.add(defaultParser.key());
                }
            }
        }

        return new Provider(wantedFeedback, testFeedback, DefaultFB.load(language), testFeedbackForAll);
    }


}
