package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EqualOperator extends AbstractOperator {

    public EqualOperator() {
        super("==", -1, ElementType.equalOperator);
    }

    public EqualOperator(int offset) {
        super("==", offset, ElementType.equalOperator);
    }

    public static EqualOperator create() {
        return new EqualOperator(-1);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitEqual(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new EqualExpr(left, this, right);
    }
}
