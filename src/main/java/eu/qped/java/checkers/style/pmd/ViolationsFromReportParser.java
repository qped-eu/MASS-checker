package eu.qped.java.checkers.style.pmd;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.qped.java.checkers.style.StyleViolation;


public class ViolationsFromReportParser {


    protected ViolationsFromReportParser(){
    }

    public static ViolationsFromReportParser createViolationsFromReportParser() {
        return new ViolationsFromReportParser();
    }
    /**
     * Json Parser
     * @return Violation List from the Json File
     */
    public ArrayList<StyleViolation> parse () {
        ArrayList<StyleViolation> violations = new ArrayList<>();
        try {
            Object obj = JsonParser.parseReader(new FileReader("src/main/java/eu/qped/java/checkers/style/resources/report.json"));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray files = (JsonArray) jsonObject.get("files");
            for (int i = 0; i < files.size(); i++) {
                JsonObject tempClass = files.get(i).getAsJsonObject();
                JsonArray tempViolations =  (JsonArray) tempClass.get("violations");
                violations = new ArrayList<>();
                for (int j = 0; j < tempViolations.size(); j++) {
                    JsonObject tempJsonObj = (JsonObject) tempViolations.get(j);
                    violations.add(new StyleViolation(tempJsonObj.get("rule").toString() ,tempJsonObj.get("description").toString(),tempJsonObj.get("beginline").getAsInt() ,tempJsonObj.get("priority").getAsInt() ));
                }
            }
        }
        catch (IOException e){
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
        }
        return violations;
    }
}
