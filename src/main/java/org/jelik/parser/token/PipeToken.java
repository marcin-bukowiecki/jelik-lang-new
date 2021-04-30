package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class PipeToken extends Token {

    public PipeToken(int row, int col) {
        super("|", row, col, ElementType.pipe);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitPipe(this, parseContext);
    }
}
