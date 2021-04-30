package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class SingleApostropheToken extends Token {

    public SingleApostropheToken(int row, int col) {
        super("'", row, col, ElementType.singleApostrophe);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitSingleApostropheToken(this, parseContext);
    }
}
