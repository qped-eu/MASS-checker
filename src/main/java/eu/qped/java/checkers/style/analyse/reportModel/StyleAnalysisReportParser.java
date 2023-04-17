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
