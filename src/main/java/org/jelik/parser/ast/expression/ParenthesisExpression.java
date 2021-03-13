package org.jelik.parser.ast.expression;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ParenthesisExpression extends ExpressionReferencingType {

    @Getter
    private final LeftParenthesisToken left;

    @Getter
    private final Expression expression;

    @Getter
    private final RightParenthesisToken right;

    public ParenthesisExpression(LeftParenthesisToken left, Expression expression, RightParenthesisToken right) {
        this.left = left;
        if (expression != null) {
            expression.setParent(this);
            this.expression = expression;
        } else {
            this.expression = EmptyExpression.INSTANCE;
        }
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
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "" + left + expression + right;
    }
}
