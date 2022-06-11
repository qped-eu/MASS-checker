package eu.qped.java.checkers.style.settings;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.QFStyleSettings;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import java.util.List;

/**
 * Style checker configs manager.
 * The class manage all the settings for the style checker and create a Setting Object to control the checker.
 *
 * @author Basel Alaktaa
 * @version 1.1
 * @since 01.06.2021
 */
@Data
@NoArgsConstructor
@Builder
public class StyleConfigurationReader {


    private QFStyleSettings qfStyleSettings;

    public StyleSettings getStyleSettings() {
        StyleSettings styleSettings = new StyleSettings();
        try {
            styleSettings.setMaxClassLength(qfStyleSettings.getClassLength() != null ? Integer.parseInt(qfStyleSettings.getClassLength()) : -1);
            styleSettings.setMaxMethodLength(qfStyleSettings.getMethodLength() != null ? Integer.parseInt(qfStyleSettings.getMethodLength()) : -1);
            styleSettings.setMaxFieldsCount(qfStyleSettings.getFieldsCount() != null ? Integer.parseInt(qfStyleSettings.getFieldsCount()) : -1);
            styleSettings.setMaxCycloComplexity(qfStyleSettings.getCycloComplexity() != null ? Integer.parseInt(qfStyleSettings.getCycloComplexity()) : -1);
            styleSettings.setVarNamesRegEx(qfStyleSettings.getVarName() != null ? qfStyleSettings.getVarName() : "undefined");
            styleSettings.setMethodNamesRegEx(qfStyleSettings.getMethodName() != null ? qfStyleSettings.getMethodName() : "undefined");
            styleSettings.setClassNameRegEx(qfStyleSettings.getClassName() != null ? qfStyleSettings.getClassName() : "undefined");
            styleSettings.setMainLevel(isCheckLevel(qfStyleSettings.getMainLevel()) ? (CheckLevel.valueOf(qfStyleSettings.getMainLevel())) : CheckLevel.BEGINNER);
            styleSettings.setNamesLevel(isCheckLevel(qfStyleSettings.getNamesLevel()) ? (CheckLevel.valueOf(qfStyleSettings.getNamesLevel())) : CheckLevel.BEGINNER);
            styleSettings.setComplexityLevel(isCheckLevel(qfStyleSettings.getCompLevel()) ? (CheckLevel.valueOf(qfStyleSettings.getCompLevel())) : CheckLevel.BEGINNER);
            styleSettings.setBasisLevel(isCheckLevel(qfStyleSettings.getBasisLevel()) ? (CheckLevel.valueOf(qfStyleSettings.getBasisLevel())) : CheckLevel.BEGINNER);
        } catch (IllegalArgumentException | NullPointerException e) {
            LogManager.getLogger(getClass()).warn(e.getMessage());
        }
        return styleSettings;
    }

    public boolean isCheckLevel(String checkLevel) {
        try {
            List<CheckLevel> checkLevels = List.of(CheckLevel.BEGINNER, CheckLevel.INTERMEDIATE, CheckLevel.ADVANCED);
            return checkLevels.contains(CheckLevel.valueOf(checkLevel));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

