package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.AndExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class AndOperator extends AbstractOperator {

    public AndOperator(int row, int col) {
        super("and", row, col, ElementType.andOperator);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitAnd(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new AndExpr(left, this, right);
    }
}
