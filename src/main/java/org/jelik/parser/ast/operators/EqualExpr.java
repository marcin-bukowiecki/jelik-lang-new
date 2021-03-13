package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class EqualExpr extends AbstractLogicalOpExpr {

    public EqualExpr(Expression left, AbstractOperator abstractOperator, Expression right) {
        super(left, abstractOperator, right);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
