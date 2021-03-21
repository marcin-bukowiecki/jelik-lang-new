package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ElseKeyword extends Token {

    public ElseKeyword(int row, int col) {
        super("else", row, col, ElementType.elseKeyword);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitElseKeyword(this, parseContext);
    }

    @Override
    public String toString() {
        return "else";
    }
}
