package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.ast.operators.RemExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class RemOperator extends AbstractOperator {

    public RemOperator(int row, int col) {
        super("%", row, col, ElementType.rem);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitRem(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new RemExpr(left, this, right);
    }
}