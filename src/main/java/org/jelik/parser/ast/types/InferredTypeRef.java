package org.jelik.parser.ast.types;

import lombok.Getter;
import org.jelik.parser.ast.Expression;
import org.jelik.types.Type;

@Getter
public class InferredTypeRef extends TypeRef {

    private final Expression ref;

    public InferredTypeRef(Expression ref) {
        this.ref = ref;
    }

    @Override
    public Type getType() {
        return ref.getReturnType();
    }

    @Override
    public Type getGenericType() {
        return ref.getGenericReturnType();
    }

    @Override
    public void setType(Type type) {
        throw new UnsupportedOperationException("Can't set type for inferred type");
    }

    @Override
    public void setGenericType(Type type) {
        throw new UnsupportedOperationException("Can't set type for inferred type");
    }
}
