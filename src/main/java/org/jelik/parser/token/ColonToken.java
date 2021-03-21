package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ColonToken extends Token {

    public ColonToken(int row, int col) {
        super(":", row, col, ElementType.colon);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitColon(this, parseContext);
    }
}
