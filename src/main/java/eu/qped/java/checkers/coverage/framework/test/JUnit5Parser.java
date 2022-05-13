package eu.qped.java.checkers.coverage.framework.test;

import java.util.*;
import java.util.regex.*;


public class JUnit5Parser {
    private static final int WANT_MATCH = 1, GOT_MATCH = 2;
    private final Pattern pattern = Pattern.compile(".*<(.*)>.*<(.*)>");
    private Matcher matcher;
    private boolean notLocked = false;


    protected boolean parse(String string) {
        notLocked = false;
        if (Objects.nonNull(string)) {
            matcher = pattern.matcher(string);
            notLocked = matcher.find();
        }
        return notLocked;
    }

    protected String want() {
        if (notLocked) return matcher.group(WANT_MATCH);
        return null;
    }

    protected String got() {
        if (notLocked) return matcher.group(GOT_MATCH);
        return null;
    }

}
