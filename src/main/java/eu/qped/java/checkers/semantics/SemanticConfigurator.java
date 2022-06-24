package eu.qped.java.checkers.semantics;

import eu.qped.java.checkers.mass.QFSemSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticConfigurator {

    private String filePath;
    private String methodName;
    private Boolean recursionAllowed;
    private Integer whileLoop;
    private Integer forLoop;
    private Integer forEachLoop;
    private Integer ifElseStmt;
    private Integer doWhileLoop;
    private String returnType;

    private QFSemSettings qfSemSettings;


    private SemanticConfigurator(QFSemSettings qfSemSettings) {
        this.qfSemSettings = qfSemSettings;
        setDefaults();
        applySettings();
    }

    public Map<String, List<SemanticSettingItem>> groupByFileName() {
        return
                qfSemSettings.getSemanticSettings()
                        .stream()
                        .collect(Collectors.groupingBy(SemanticSettingItem::getFilePath));

    }

    public static SemanticConfigurator createSemanticConfigurator(QFSemSettings qfSemSettings) {
        return new SemanticConfigurator(qfSemSettings);
    }

    private void setDefaults() {
        setMethodName("undefined");
        setRecursionAllowed(false);
        setReturnType("undefined");
        setWhileLoop(-1);
        setForLoop(-1);
        setForEachLoop(-1);
        setIfElseStmt(-1);
        setDoWhileLoop(-1);
    }

    private void applySettings() {

    }

}
