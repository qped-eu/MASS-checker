package eu.qped.java.checkers.semantics.configs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class SemanticFileConfig {
    private String filePath;
    private List<SemanticMethodConfig> methodConfigs;
}
