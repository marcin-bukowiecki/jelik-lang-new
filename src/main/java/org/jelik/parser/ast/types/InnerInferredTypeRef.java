package org.jelik.parser.ast.types;

import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionReferenceNodeImpl;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class InnerInferredTypeRef extends AbstractTypeRef {

    private final Expression ref;

    public InnerInferredTypeRef(Expression ref) {
        this.ref = ref;
    }

    @Override
    public Type getType() {
        return ref.getGenericReturnType().getTypeVariables().get(0);
    }

    @Override
    public Type getGenericType() {
        return ref.getGenericReturnType().getTypeVariables().get(0);
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
}
