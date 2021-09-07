package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.Statement;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class AssignExpr extends AbstractOpExpr implements Statement {

    public AssignExpr(Expression left, AbstractOperator op, Expression right) {
        super(left, op, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
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
