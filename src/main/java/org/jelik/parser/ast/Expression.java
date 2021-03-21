package org.jelik.parser.ast;

import org.jelik.parser.ast.context.NodeContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Base class for all expressions
 *
 * @author Marcin Bukowiecki
 */
public abstract class Expression extends ASTNode {

    protected Expression furtherExpression;

    public boolean ignored;

    public Expression() {

    }

    public void replace(Expression newNode) {
        parent.replaceWith(this, newNode);
    }

    public void setFurtherExpression(@Nullable Expression furtherExpression) {
        if (furtherExpression == null) {
            return;
        }
        this.furtherExpression = furtherExpression;
        furtherExpression.setParent(this);
    }

    public Optional<Expression> getFurtherExpressionOpt() {
        return Optional.ofNullable(furtherExpression);
    }

    public Expression getFurtherExpression() {
        return furtherExpression;
    }

    public abstract NodeContext getNodeContext();

    public abstract void setType(Type type);

    public abstract void setGenericType(Type type);

    /**
     * Returns type of last element of expression i.e. a.foo.bar will return type from bar
     *
     * @return org.jelik.types.Type
     */
    public abstract Type getReturnType();

    /**
     * Returns generic type of last element of expression i.e. a.foo.bar will return type from bar
     *
     * @return org.jelik.types.Type
     */
    public abstract Type getGenericReturnType();

    /**
     * Returns type of this node i.e. expression a.foo.bar will return type of a
     *
     * @return org.jelik.types.Type
     */
    public abstract Type getType();

    /**
     * Returns generic type of this node i.e. expression a.foo.bar will return type of a
     *
     * @return org.jelik.types.Type
     */
    public abstract Type getGenericType();

    public void clearFurtherExpression() {
        this.furtherExpression = null;
    }
}
