package org.jelik.parser.ast.expression;

import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.context.TypedNodeContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for node with an {@link Type}
 *
 * @author Marcin Bukowiecki
 */
public abstract class ExpressionWithType extends Expression {

    protected final TypedNodeContext nodeContext = new TypedNodeContext();

    public ExpressionWithType() {
        super();
    }

    @Override
    @NotNull
    public TypedNodeContext getNodeContext() {
        return nodeContext;
    }

    @Override
    public Type getType() {
        return this.nodeContext.getType();
    }

    @Override
    public Type getGenericType() {
        return this.nodeContext.getGenericType();
    }

    @Override
    public Type getReturnType() {
        if (furtherExpression == null) {
            return getType();
        } else {
            return furtherExpression.getReturnType();
        }
    }

    @Override
    public Type getGenericReturnType() {
        if (furtherExpression == null) {
            return getGenericType();
        } else {
            return furtherExpression.getGenericReturnType();
        }
    }

    @Override
    public void setType(Type type) {
        this.nodeContext.setType(type);
    }

    @Override
    public void setGenericType(Type type) {
        this.nodeContext.setGenericType(type);
    }
}
