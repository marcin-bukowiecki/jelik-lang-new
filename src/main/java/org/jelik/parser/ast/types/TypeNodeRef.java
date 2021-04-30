package org.jelik.parser.ast.types;

import lombok.Getter;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class TypeNodeRef extends AbstractTypeRef {

    private final TypeNode typeNode;

    public TypeNodeRef(TypeNode typeNode) {
        this.typeNode = typeNode;
    }

    @Override
    public Type getType() {
        return typeNode.getType();
    }

    @Override
    public Type getGenericType() {
        return typeNode.getGenericType();
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
