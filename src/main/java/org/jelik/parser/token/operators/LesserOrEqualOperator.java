package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.ast.operators.LesserOrEqualExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Token for '>='
 *
 * @author Marcin Bukowiecki
 */
public class LesserOrEqualOperator extends AbstractOperator {

    public LesserOrEqualOperator(int row, int col) {
        super("<=", row, col, ElementType.lesserOrEqualOperator);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new LesserOrEqualExpr(left, this, right);
    }
}
