package eu.qped.java.checkers.coverage;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.coverage.enums.ModifierType;
import java.util.*;


public class QfCovSetting extends QfObjectBase {
    private Set<ModifierType> excludeByTypeSet = new HashSet<>();
    private Set<String> excludeByNameSet = new HashSet<>();
    private List<String> feedback = new LinkedList<>();
    private String format = null;
    private String convention = "JAVA";

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public Set<ModifierType> getExcludeByTypeSet() {
        return excludeByTypeSet;
    }

    public void setExcludeByTypeSet(Set<ModifierType> excludeByTypeSet) {
        this.excludeByTypeSet = excludeByTypeSet;
    }

    public Set<String> getExcludeByNameSet() {
        return excludeByNameSet;
    }

    public void setExcludeByNameSet(Set<String> excludeByNameSet) {
        this.excludeByNameSet = excludeByNameSet;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public void setConvention(String convention) {
        this.convention = convention;
    }

    public String getConvention() {
        return convention;
    }

}
