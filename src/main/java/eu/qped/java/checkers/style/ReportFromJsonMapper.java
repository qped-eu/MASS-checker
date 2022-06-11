package eu.qped.java.checkers.style;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.checkers.style.reportModel.StyleCheckReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
@AllArgsConstructor
@Builder
public class ReportFromJsonMapper {

    public StyleCheckReport readJson() throws IOException {
        ObjectMapper jacksonMapper = new ObjectMapper();
        File jsonFile = new File("src/main/java/eu/qped/java/checkers/style/resources/report.json").getAbsoluteFile();
        return jacksonMapper.readValue(jsonFile, StyleCheckReport.class);
    }

}
