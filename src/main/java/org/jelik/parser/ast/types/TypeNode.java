package org.jelik.parser.ast.types;

import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.context.TypedNodeContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for parsed Type
 *
 * @author Marcin Bukowiecki
 */
public abstract class TypeNode extends ASTNodeImpl {

    protected final TypedNodeContext nodeContext = new TypedNodeContext();

    public TypeNode() {
    }

    public Type getType() {
        return nodeContext.getType();
    }

    public void setType(@NotNull Type type) {
        nodeContext.setType(type);
    }

    public Type getGenericType() {
        return nodeContext.getGenericType();
    }

    public void setGenericType(@NotNull Type type) {
        nodeContext.setGenericType(type);
    }

    public abstract String getSymbol();

    public abstract int getEndOffset();

    public boolean isMaybe() {
        return false;
    }

    public boolean isInferred() {
        return false;
    }
}
