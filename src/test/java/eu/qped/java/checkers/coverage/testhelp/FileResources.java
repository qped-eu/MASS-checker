package eu.qped.java.checkers.coverage.testhelp;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileResources {

    public static Map<String, File> filesByClassName = new HashMap<>() {{
        put("test.IfStmt", Paths.get("coverage_testclasses/ast/IfStmt.java").toFile());
        put("test.CaseStmt", Paths.get("coverage_testclasses/ast/CaseStmt.java").toFile());
        put("test.Constructor", Paths.get("coverage_testclasses/ast/Constructor.java").toFile());
        put("test.LoopStmt", Paths.get("coverage_testclasses/ast/LoopStmt.java").toFile());

        put("adt.Bag", Paths.get("coverage_testclasses/checker/Bag.java").toFile());
        put("case_no_constructor", Paths.get("coverage_testclasses/checker/case_no_constructor/BagTest.java").toFile());
        put("case_no_method", Paths.get("coverage_testclasses/checker/case_no_method/BagTest.java").toFile());
        put("case_all_method", Paths.get("coverage_testclasses/checker/case_all_method/BagTest.java").toFile());
        put("case_all_stmt", Paths.get("coverage_testclasses/checker/case_all_stmt/BagTest.java").toFile());

        put("test.TestFramework", Paths.get("coverage_testclasses/test/TestFramework.class").toFile());
        put("test.TestFrameworkIsCorrect", Paths.get("coverage_testclasses/test/TestFrameworkIsCorrect.class").toFile());
        put("test.TestFrameworkIsWrong", Paths.get("coverage_testclasses/test/TestFrameworkIsWrong.class").toFile());
    }};

    public static Map<String, String> convertNames = new HashMap<>() {{
        put("case_no_method", "adt.BagTest");
        put("case_no_constructor", "adt.BagTest");
        put("case_all_method", "adt.BagTest");
        put("case_all_stmt", "adt.BagTest");
    }};

}
