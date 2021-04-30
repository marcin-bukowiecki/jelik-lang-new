package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class AssignOperator extends AbstractOperator {

    public AssignOperator(int row, int col) {
        super("=", row, col, ElementType.assign);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitAssign(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new AssignExpr(left, this, right);
    }
}
