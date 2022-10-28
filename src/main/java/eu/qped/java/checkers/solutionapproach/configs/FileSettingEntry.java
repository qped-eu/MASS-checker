package eu.qped.java.checkers.solutionapproach.configs;

import eu.qped.java.checkers.checkerabstract.AbstractSetting;
import eu.qped.java.checkers.solutionapproach.SemanticSettingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class FileSettingEntry extends AbstractSetting {

    private String filePath;

    private List<SemanticSettingItem> settingItems;

}
