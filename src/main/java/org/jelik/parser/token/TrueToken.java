package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * True literal for Boolean type
 *
 * @author Marcin Bukowiecki
 */
public class TrueToken extends Token {

    public TrueToken(int row, int col) {
        super("True", row, col, ElementType.trueLiteral);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }
}
