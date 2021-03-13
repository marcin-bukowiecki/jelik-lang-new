package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReturnExpressionVisitor implements ParseVisitor<Expression> {

    private final ReturnKeyword returnKeyword;

    public ReturnExpressionVisitor(ReturnKeyword returnKeyword) {
        this.returnKeyword = returnKeyword;
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        return new ReturnExpr(returnKeyword, new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext));
    }
}
