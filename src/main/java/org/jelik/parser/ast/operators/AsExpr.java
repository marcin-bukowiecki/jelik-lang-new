package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
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
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
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
