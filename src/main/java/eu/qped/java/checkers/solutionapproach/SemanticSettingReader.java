package eu.qped.java.checkers.solutionapproach;

import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.configs.FileSettingEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticSettingReader {

    private final static boolean REC_ALLOWED_DEFAULT = false;
    private final static int MAX_STMT_DEFAULT = -1;

    private QfSemanticSettings qfSemanticSettings;

    private void setDefault() {
        qfSemanticSettings.getSemantics().forEach(
                semanticSettingItem -> {
                    if (semanticSettingItem.getRecursive() == null) {
                        semanticSettingItem.setRecursive(REC_ALLOWED_DEFAULT);
                    }
                    if (semanticSettingItem.getWhileLoop() == null) {
                        semanticSettingItem.setWhileLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getDoWhileLoop() == null) {
                        semanticSettingItem.setDoWhileLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getForLoop() == null) {
                        semanticSettingItem.setForLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getForEachLoop() == null) {
                        semanticSettingItem.setForEachLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getIfElseStmt() == null) {
                        semanticSettingItem.setIfElseStmt(MAX_STMT_DEFAULT);
                    }
                }
        );

    }

    public List<FileSettingEntry> groupByFileName() {
        setDefault();

        List<FileSettingEntry> result = new ArrayList<>();

        qfSemanticSettings.getSemantics()
                .stream()
                .collect(Collectors.groupingBy(SemanticSettingItem::getFilePath))
                .forEach((filePath, semanticSettingItems) ->
                        result.add(
                                FileSettingEntry.builder()
                                        .filePath(filePath)
                                        .settingItems(semanticSettingItems).build()
                        )
                );

        return result;
    }

}
