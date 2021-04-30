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

    private final int row;

    private final int col;

    protected final String text;

    private final ElementType elementType;

    public Token(@NotNull String text, int row, int col, @NotNull ElementType elementType) {
        this.row = row;
        this.col = col;
        this.text = text;
        this.elementType = elementType;
    }

    public boolean isDummy() {
        return row == -1;
    }

    public abstract void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext);

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getEndCol() {
        return col + getText().length();
    }

    public int getEndRow() {
        return row;
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

    public boolean isWhiteSpace() {
        return elementType == ElementType.whitespace;
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
