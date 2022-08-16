package eu.qped.java.checkers.coverage;

import eu.qped.framework.FileInfo;
import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.coverage.enums.ModifierType;
import java.util.*;
import java.util.stream.Collectors;


public class QfCovSetting extends QfObjectBase {
    private List<ModifierType> excludeByTypeSet = new LinkedList<>();
    private List<String> excludeByNameSet = new LinkedList<>();
    private List<String> feedback = new LinkedList<>();
    private String format = null;
    private String convention = "JAVA";
    private String answer = null;
    private FileInfo file = null;
    private String privateImplementation = null;
    private String language = "de";
    private boolean useBlock = true;

    public boolean isUseBlock() {
        return useBlock;
    }

    public void setUseBlock(boolean useBlock) {
        this.useBlock = useBlock;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public Set<ModifierType> getExcludeByType() {
        return excludeByTypeSet.stream().collect(Collectors.toSet());
    }

    public List<ModifierType> getExcludeByTypeSet() {
        return excludeByTypeSet;
    }

    public void setExcludeByTypeSet(List<ModifierType> excludeByTypeSet) {
        this.excludeByTypeSet = excludeByTypeSet;
    }
    public Set<String> getExcludeByName() {
        return excludeByNameSet.stream().collect(Collectors.toSet());
    }

    public List<String> getExcludeByNameSet() {
        return excludeByNameSet;
    }

    public void setExcludeByNameSet(List<String> excludeByNameSet) {
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public FileInfo getFile() {
        return file;
    }

    public void setFile(FileInfo file) {
        this.file = file;
    }

    public void setPrivateImplementation(String privateImplementation) {
        this.privateImplementation = privateImplementation;
    }

    public String getPrivateImplementation() {
        return privateImplementation;
    }
}
