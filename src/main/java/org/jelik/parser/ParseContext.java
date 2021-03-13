package org.jelik.parser;

/**
 * @author Marcin Bukowiecki
 */
public class ParseContext {

    private final Lexer lexer;

    public ParseContext(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseContext(String content) {
        this.lexer = new Lexer(new Scanner(new CharPointer(content)));
    }

    public Lexer getLexer() {
        return lexer;
    }

    public String getCurrentFilePath() {
        return lexer.getScanner().getCp().getFileAbsolutePath();
    }
}
