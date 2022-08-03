package eu.qped.java.checkers.coverage;

import eu.qped.framework.CheckerRunner;
import eu.qped.framework.qf.QfObject;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CoverageCheckerSystemTest {

    // TEST_SCHEMA:: CONVENTION<JAVA, MAVEB>_ANSWER<STR=STRING, ZIP=ZIP FOLDER >_TEACHER<T=TESTKLASS, K=KLASS>_STUDENT<T=TESTKLASS, K=KLASS>
    @Test
    public void Test_Tutorial_dry_run() {
        systemTestValue[] testValues = new systemTestValue[] {
                new systemTestValue("TEST 1: JAVA_STR_KT","coverage_testclasses/tutorial-dry-run/JAVA_STR_KT.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
                new systemTestValue("TEST 2: JAVA_STR_TK","coverage_testclasses/tutorial-dry-run/JAVA_STR_TK.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
                new systemTestValue("TEST 3: JAVA_ZIP_KT","coverage_testclasses/tutorial-dry-run/JAVA_ZIP_KT.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
                new systemTestValue("TEST 4: JAVA_ZIP_TK","coverage_testclasses/tutorial-dry-run/JAVA_ZIP_TK.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
                new systemTestValue("TEST 5: MAVEN_ZIP_TK","coverage_testclasses/tutorial-dry-run/MAVEN_ZIP_TK.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
                new systemTestValue("TEST 6: MAVEN_ZIP_KT","coverage_testclasses/tutorial-dry-run/MAVEN_ZIP_KT.json", new String[] {"---In class **EvenTest** the test method **test** failed. The expected value is **false** but was actually **true**.---You only tested the method with even numbers."}),
        };

        for (final systemTestValue value : testValues) {
            assertDoesNotThrow(() -> systemTest(value), value.name);
        }
    }

    @Test
    public void Test_bag() {
        systemTestValue[] testValues = new systemTestValue[] {
                new systemTestValue("TEST 1: JAVA_STR_KT","coverage_testclasses/bag/JAVA_STR_KT.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 2: JAVA_STR_TK","coverage_testclasses/bag/JAVA_STR_TK.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 3: JAVA_ZIP_KT","coverage_testclasses/bag/JAVA_ZIP_KT.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 4: JAVA_ZIP_TK","coverage_testclasses/bag/JAVA_ZIP_TK.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 5: MAVEN_ZIP_TK","coverage_testclasses/bag/MAVEN_ZIP_TK.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 6: MAVEN_ZIP_KT","coverage_testclasses/bag/MAVEN_ZIP_KT.json", new String[] {"---Equals method: You have not tested the equals method with an empty bag as parameter."}),
                new systemTestValue("TEST 7: EXCLUDE_METHOD_JAVA_ZIP_TK","coverage_testclasses/bag/EXCLUDE_METHOD_JAVA_ZIP_TK.json", new String[] {""}),
                new systemTestValue("TEST 7: EXCLUDE_CLASS_JAVA_ZIP_TK","coverage_testclasses/bag/EXCLUDE_CLASS_JAVA_ZIP_TK.json", new String[] {""})
        };

        for (final systemTestValue value : testValues) {
            assertDoesNotThrow(() -> systemTest(value), value.name);
        }
    }



    class systemTestValue {
        public final String name;
        public final String qfname;
        public final String[] want;

        public systemTestValue(String name, String qfname, String[] want) {
            this.name = name;
            this.qfname = qfname;
            this.want = want;
        }
    }

    public void systemTest(systemTestValue value) {
        try {
            Path qfJson = Path.of("qf.json");
            Files.copy(
                    Path.of(value.qfname),
                    qfJson,
                    REPLACE_EXISTING);

            CheckerRunner runner = new CheckerRunner(qfJson.toFile());
            runner.check();
            QfObject qfObject = runner.getQfObject();
            assertNotNull(qfObject);
            String[] got = qfObject.getFeedback();
            StringBuilder errMsg = new StringBuilder();
            errMsg.append(value.name).append("\n\n");
            Arrays.stream(got).forEach(errMsg::append);
            got = Arrays.stream(got).map(feedback -> feedback.replace("\n", "")).toArray(String[]::new);
            assertArrayEquals(value.want , got, errMsg.toString());
        } catch (Exception e) {
            assertTrue(false, e.getMessage());
            e.printStackTrace();
        }
    }


}
