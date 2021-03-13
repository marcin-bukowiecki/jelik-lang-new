package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Token for '>'
 *
 * @author Marcin Bukowiecki
 */
public class GreaterOperator extends AbstractOperator {

    public GreaterOperator(int row, int col) {
        super(">", row, col, ElementType.greaterOperator);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new GreaterExpr(left, this, right);
    }
}
