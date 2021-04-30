package org.jelik.parser.ast.expression;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ParenthesisExpression extends ExpressionWrapper {

    @Getter
    private final LeftParenthesisToken left;

    @Getter
    private final RightParenthesisToken right;

    public ParenthesisExpression(LeftParenthesisToken left, Expression expression, RightParenthesisToken right) {
        super(expression == null ? EmptyExpression.INSTANCE : expression);
        this.left = left;
        this.right = right;
    }

    @Override
    public int getStartCol() {
        return left.getCol();
    }

    @Override
    public int getStartRow() {
        return left.getRow();
    }

    @Override
    public int getEndRow() {
        return right.getEndRow();
    }

    @Override
    public int getEndCol() {
        return right.getEndCol();
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
