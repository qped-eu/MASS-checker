package eu.qped.java.utils.compiler;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to create option beans for the compiler.
 */
@NoArgsConstructor
public class CompilerSettingsFactory {


    public static List<String> createDefaultCompilerSettings() {
        List<String> defaultSettings = new ArrayList<>();
        defaultSettings.add("-verbose");
        defaultSettings.add("-Xlint");
        defaultSettings.add("-g");
        return defaultSettings;
    }


}
