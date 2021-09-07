package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Base Lexer Token
 *
 * @author Marcin Bukowiecki
 */
public abstract class Token {

    private final int startOffset;

    protected final String text;

    private final ElementType elementType;

    public Token(@NotNull String text, int startOffset, @NotNull ElementType elementType) {
        this.startOffset = startOffset;
        this.text = text;
        this.elementType = elementType;
    }

    public abstract void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext);

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return startOffset + text.length();
    }

    public @NotNull String getText() {
        return text;
    }

    public @NotNull ElementType getTokenType() {
        return elementType;
    }

    @Override
    public String toString() {
        return text;
    }

    public final boolean isWhiteSpace() {
        return elementType == ElementType.whitespace || elementType == ElementType.newLine;
    }

    public boolean isOperator() {
        return false;
    }

    public boolean canBeType() {
        switch (getTokenType()) {
            case leftBracket:
            case literal:
                return true;
            default:
                return false;
        }
    }
}
