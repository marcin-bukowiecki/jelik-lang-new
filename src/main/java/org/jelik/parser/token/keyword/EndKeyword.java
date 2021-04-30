package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EndKeyword extends Token {

    public EndKeyword(int row, int col) {
        super("end", row, col, ElementType.endKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public String toString() {
        return "end";
    }
}
