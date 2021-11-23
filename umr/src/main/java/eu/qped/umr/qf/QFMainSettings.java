package eu.qped.umr.qf;

public class QFMainSettings {

    private String syntaxLevel;
    private String preferredLanguage;
    private String styleNeeded;
    private String semanticNeeded;

    public String getSyntaxLevel() {
        return syntaxLevel;
    }

    public void setSyntaxLevel(String syntaxLevel) {
        this.syntaxLevel = syntaxLevel;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getStyleNeeded() {
        return styleNeeded;
    }

    public void setStyleNeeded(String styleNeeded) {
        this.styleNeeded = styleNeeded;
    }

    public String getSemanticNeeded() {
        return semanticNeeded;
    }

    public void setSemanticNeeded(String semanticNeeded) {
        this.semanticNeeded = semanticNeeded;
    }
}
