package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class AddOperator extends AbstractOperator {

    public AddOperator(int row, int col) {
        super("+", row, col, ElementType.add);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitAdd(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new AddExpr(left, this, right);
    }
}
