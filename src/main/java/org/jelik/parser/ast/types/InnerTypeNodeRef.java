package org.jelik.parser.ast.types;

import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class InnerTypeNodeRef extends AbstractTypeRef {

    private final TypeNode typeNode;

    public InnerTypeNodeRef(TypeNode typeNode) {
        this.typeNode = typeNode;
    }

    @Override
    public Type getType() {
        return typeNode.getType().getInnerType(0);
    }

    @Override
    public Type getGenericType() {
        return typeNode.getGenericType().getInnerType(0);
    }

    @Override
    public void setType(@NotNull Type type) {
        this.typeNode.setType(type);
    }

    @Override
    public void setGenericType(@NotNull Type type) {
        this.typeNode.setGenericType(type);
    }
}
