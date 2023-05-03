package eu.qped.java.checkers.style;


import eu.qped.java.checkers.mass.QfStyleSettings;
import eu.qped.java.checkers.style.analyse.StyleAnalyser;
import eu.qped.java.checkers.style.config.StyleConfigurationReader;
import eu.qped.java.checkers.style.config.StyleSettings;
import eu.qped.java.checkers.style.feedback.StyleFeedbackGenerator;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleChecker {

    private String targetPath;
    @NonNull
    private QfStyleSettings qfStyleSettings;
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private StyleSettings styleSettings;
    private StyleAnalyser styleAnalyser;
    private StyleFeedbackGenerator styleFeedbackGenerator;


    /**
     * Checks the style settings for a particular path, runs an analysis on the style and then generates feedbacks based
     * on the report of the analysis and the style settings.
     * @return a List of Strings containing the feedbacks generated from the analysis
     */
    public List<String> check() {
        styleSettings = readStyleSettings();
        if (styleAnalyser == null) {
            styleAnalyser = StyleAnalyser.builder()
                    .styleSettings(styleSettings)
                    .targetPath(targetPath)
                    .build()
            ;
        }
        var styleReport = styleAnalyser.analyse();
        if (styleFeedbackGenerator == null) {
            styleFeedbackGenerator = StyleFeedbackGenerator.builder().build();
        }
        return styleFeedbackGenerator.generateFeedbacks(styleReport, styleSettings);
    }

    private StyleSettings readStyleSettings() {
        StyleConfigurationReader styleConfigurationReader = StyleConfigurationReader.builder().qfStyleSettings(qfStyleSettings).build();
        styleSettings = styleConfigurationReader.getStyleSettings();
        return styleSettings;
    }


}
