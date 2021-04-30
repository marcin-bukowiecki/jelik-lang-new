package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ElifKeyword extends Token {

    public ElifKeyword(int row, int col) {
        super("elif", row, col, ElementType.elifKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitElifKeyword(this, parseContext);
    }

    @Override
    public String toString() {
        return "elif";
    }
}
