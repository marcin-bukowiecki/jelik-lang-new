package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.visitors.conditions.IfVisitor;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReturnExpressionVisitor implements TokenVisitor<Expression> {

    private final ReturnKeyword returnKeyword;

    public ReturnExpressionVisitor(ReturnKeyword returnKeyword) {
        this.returnKeyword = returnKeyword;
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        var next = parseContext.getLexer().nextToken();
        if (next instanceof IfKeyword) {
            return new ReturnExpr(returnKeyword, new IfVisitor(((IfKeyword) next)).visit(parseContext));
        }
        return new ReturnExpr(returnKeyword, new ExpressionVisitor(next).visit(parseContext));
    }
}
