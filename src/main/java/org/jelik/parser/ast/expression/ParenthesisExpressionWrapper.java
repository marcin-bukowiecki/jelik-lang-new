package org.jelik.parser.ast.expression;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ParenthesisExpressionWrapper extends ExpressionWrapper {

    private final LeftParenthesisToken left;

    private final RightParenthesisToken right;

    public ParenthesisExpressionWrapper(LeftParenthesisToken left, Expression expression, RightParenthesisToken right) {
        super(expression == null ? EmptyExpression.INSTANCE : expression);
        this.left = left;
        this.right = right;
    }

    @Override
    public int getStartOffset() {
        return left.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return right.getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "" + left + getExpression() + right;
    }
}
