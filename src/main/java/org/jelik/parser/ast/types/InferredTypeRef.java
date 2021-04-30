package org.jelik.parser.ast.types;

import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionReferenceNodeImpl;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base type for referencing types
 *
 * @author Marcin Bukowiecki
 */
public class InferredTypeRef extends AbstractTypeRef {

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
    public void setType(@NotNull Type type) {
        throw new UnsupportedOperationException("Can't set type for inferred type");
    }

    @Override
    public void setGenericType(@NotNull Type type) {
        throw new UnsupportedOperationException("Can't set type for inferred type");
    }

    @Override
    public boolean isFunctionReference() {
        return ref instanceof FunctionReferenceNodeImpl;
    }

    public Expression getRef() {
        return ref;
    }
}
