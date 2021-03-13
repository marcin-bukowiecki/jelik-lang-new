package org.jelik.parser.ast.operators;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class AssignExpr extends AbstractOpExpr  {

    public AssignExpr(Expression left, AbstractOperator op, Expression right) {
        super(left, op, right);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public Type getGenericReturnType() {
        return right.getGenericReturnType();
    }

    @Override
    public Type getReturnType() {
        return right.getReturnType();
    }

    @Override
    public Type getType() {
        return right.getType();
    }

    @Override
    public Type getGenericType() {
        return right.getGenericType();
    }
}
