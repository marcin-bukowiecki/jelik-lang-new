package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.NotExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public class NotOperator extends AbstractOperator {

    public NotOperator(int row, int col) {
        super("!", row, col, ElementType.notOperator);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitNotOperator(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(@Nullable Expression left, @NotNull Expression right) {
        return new NotExpr( this, right);
    }
}
