package org.jelik.parser.ast.operators;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.operators.DivideOperator;
import org.jetbrains.annotations.NotNull;

public class DivExpr extends AbstractOpExpr {

    public DivExpr(Expression left, DivideOperator op, Expression right) {
        super(left, op, right);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}