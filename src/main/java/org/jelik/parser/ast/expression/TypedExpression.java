package org.jelik.parser.ast.expression;

import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.context.TypedNodeContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for node with an {@link Type}
 *
 * @author Marcin Bukowiecki
 */
public abstract class TypedExpression extends ASTNodeImpl implements Expression {

    protected final TypedNodeContext nodeContext = new TypedNodeContext();

    public TypedExpression() {
        super();
    }

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
        return getType();
    }

    @Override
    public Type getGenericReturnType() {
        return getGenericType();
    }

    public void setType(@NotNull Type type) {
        this.nodeContext.setType(type);
    }

    public void setGenericType(@NotNull Type type) {
        this.nodeContext.setGenericType(type);
    }
}
