package org.jelik.parser.ast.expression;

import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base class for all expressions
 *
 * @author Marcin Bukowiecki
 */
public abstract class ExpressionWrapper extends ASTNodeImpl implements Expression {

    private Expression expression;

    public ExpressionWrapper(@NotNull Expression expression) {
        this.expression = expression;
        this.expression.setParent(this);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        Objects.requireNonNull(newNode);
        assert expression == oldNode : "Old node must be same";
        this.expression = newNode;
        this.expression.setParent(this);
    }

    @Override
    public String toString() {
        return expression.toString();
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void setType(@NotNull Type type) {
        this.expression.setType(type);
    }

    @Override
    public void setGenericType(@NotNull Type type) {
        this.expression.setGenericType(type);
    }

    @Override
    public Type getReturnType() {
        return this.expression.getReturnType();
    }

    @Override
    public Type getGenericReturnType() {
        return this.expression.getGenericReturnType();
    }

    @Override
    public Type getType() {
        return this.expression.getType();
    }

    @Override
    public Type getGenericType() {
        return this.expression.getGenericType();
    }
}
