package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.LesserOrEqualOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LesserOrEqualExpr extends AbstractLogicalOpExpr {

    public LesserOrEqualExpr(Expression left, LesserOrEqualOperator lesserOrEqualOperator, Expression right) {
        super(left, lesserOrEqualOperator, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
