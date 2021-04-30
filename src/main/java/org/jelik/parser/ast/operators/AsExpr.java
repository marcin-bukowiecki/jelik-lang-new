package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.AsOperator;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class AsExpr extends AbstractOpExpr {

    public AsExpr(Expression left, AsOperator addOperator, Expression right) {
        super(left, addOperator, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public Type getReturnType() {
        return right.getReturnType();
    }

    @Override
    public Type getGenericReturnType() {
        return right.getGenericReturnType();
    }
}
