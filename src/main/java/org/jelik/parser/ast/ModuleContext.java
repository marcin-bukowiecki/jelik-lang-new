package org.jelik.parser.ast;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleContext {

    private String canonicalName;

    private String fileAbsolutePath;

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }
}
