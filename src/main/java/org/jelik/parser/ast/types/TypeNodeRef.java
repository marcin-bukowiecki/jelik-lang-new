package org.jelik.parser.ast.types;

import lombok.Getter;
import org.jelik.types.Type;

@Getter
public class TypeNodeRef extends TypeRef {

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
    public void setType(Type type) {
        this.typeNode.setType(type);
    }

    @Override
    public void setGenericType(Type type) {
        this.typeNode.setGenericType(type);
    }
}
