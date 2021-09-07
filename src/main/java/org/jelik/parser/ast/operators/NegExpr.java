package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.SubtractOperator;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class NegExpr extends SubExpr {

    public NegExpr(SubtractOperator op, Expression subject) {
        super(EmptyExpression.INSTANCE, op, subject);
    }

    @Override
    public Type getReturnType() {
        return right.getReturnType();
    }

    @Override
    public Type getGenericReturnType() {
        return right.getGenericReturnType();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
