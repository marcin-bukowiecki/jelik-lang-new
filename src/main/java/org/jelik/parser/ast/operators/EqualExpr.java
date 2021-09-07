package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.parser.token.operators.EqualOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EqualExpr extends AbstractLogicalOpExpr {

    public EqualExpr(Expression left, AbstractOperator abstractOperator, Expression right) {
        super(left, abstractOperator, right);
    }

    public EqualExpr(Expression left, Expression right) {
        super(left, new EqualOperator(), right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
