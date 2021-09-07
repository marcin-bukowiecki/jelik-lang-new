package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ElvisExprImpl extends AbstractOpExpr implements ElvisExpr {

    public ElvisExprImpl(Expression left, AbstractOperator op, Expression right) {
        super(left, op, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitElvisExpr(this, compilationContext);
    }
}
