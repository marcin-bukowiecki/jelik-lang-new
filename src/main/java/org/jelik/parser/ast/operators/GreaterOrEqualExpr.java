package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.GreaterOrEqualOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class GreaterOrEqualExpr extends AbstractLogicalOpExpr {

    public GreaterOrEqualExpr(Expression left, GreaterOrEqualOperator greaterOrEqualOperator, Expression right) {
        super(left, greaterOrEqualOperator, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
