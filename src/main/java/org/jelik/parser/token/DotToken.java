package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents .
 *
 * @author Marcin Bukowiecki
 */
public class DotToken extends Token {

    public DotToken(int row, int col) {
        super(".", row, col, ElementType.dot);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitDot(this, parseContext);
    }
}
