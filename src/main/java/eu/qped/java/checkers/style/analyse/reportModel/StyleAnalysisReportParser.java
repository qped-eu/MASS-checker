package eu.qped.java.checkers.style.analyse.reportModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
@AllArgsConstructor
@Builder
public class StyleAnalysisReportParser {

    /**
     * this method create a new jsonFile, which contains a report of the analysis
     * @return A final report of analyse of the style
     */
    public StyleAnalysisReport parse() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        File jsonFile = new File("src/main/java/eu/qped/java/checkers/style/resources/report.json").getAbsoluteFile();
        StyleAnalysisReport result;
        try {
            result = jacksonMapper.readValue(jsonFile, StyleAnalysisReport.class);
        } catch (IOException e) {
            return StyleAnalysisReport.builder().build();
        }
        return result;
    }

}
