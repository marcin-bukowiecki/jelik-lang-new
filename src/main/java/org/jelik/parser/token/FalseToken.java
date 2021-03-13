package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * False literal for Boolean type
 *
 * @author Marcin Bukowiecki
 */
public class FalseToken extends Token {

    public FalseToken(int row, int col) {
        super("False", row, col, ElementType.falseLiteral);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }
}
