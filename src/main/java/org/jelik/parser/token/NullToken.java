package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Null literal for null reference type
 *
 * @author Marcin Bukowiecki
 */
public class NullToken extends Token {

    public NullToken(int row, int col) {
        super("Null", row, col, ElementType.nullLiteral);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }
}
