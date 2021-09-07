package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.GreaterOrEqualOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GreaterOrEqualExpr extends AbstractLogicalOpExpr {

    public GreaterOrEqualExpr(Expression left, GreaterOrEqualOperator greaterOrEqualOperator, Expression right) {
        super(left, greaterOrEqualOperator, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
