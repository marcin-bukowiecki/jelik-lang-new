package org.jelik.parser.ast.operators;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.SubtractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class NegExpr extends SubExpr {

    public NegExpr(SubtractOperator op, Expression subject) {
        super(EmptyExpression.INSTANCE, op, subject);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
