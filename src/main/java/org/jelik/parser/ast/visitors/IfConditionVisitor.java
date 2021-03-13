package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.branching.IfConditionExpression;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Parses if condition if expr then ....
 *
 * @author Marcin Bukowiecki
 */
public class IfConditionVisitor implements ParseVisitor<IfConditionExpression> {

    private final Token current;

    public IfConditionVisitor(Token current) {
        this.current = current;
    }

    @Override
    public @NotNull IfConditionExpression visit(@NotNull ParseContext parseContext) {
        return new IfConditionExpression(new ExpressionVisitor(current).visit(parseContext));
    }
}
