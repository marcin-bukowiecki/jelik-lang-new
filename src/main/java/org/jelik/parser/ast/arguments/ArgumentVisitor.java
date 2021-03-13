package org.jelik.parser.ast.arguments;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ArgumentVisitor implements ParseVisitor<Argument> {

    private final Token start;

    public ArgumentVisitor(Token start) {
        this.start = start;
    }

    @Override
    public @NotNull Argument visit(@NotNull ParseContext parseContext) {
        var expr = new ExpressionVisitor(start).visit(parseContext);
        return new Argument(expr);
    }
}
