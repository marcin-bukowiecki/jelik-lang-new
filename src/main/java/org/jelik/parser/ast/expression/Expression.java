package org.jelik.parser.ast.expression;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base type for all expressions
 *
 * @author Marcin Bukowiecki
 */
public interface Expression extends ASTNode {

    /**
     * Returns type of last element of expression i.e. a.foo.bar will return type from bar
     *
     * @return org.jelik.types.Type
     */
    Type getReturnType();

    /**
     * Returns generic type of last element of expression i.e. a.foo.bar will return type from bar
     *
     * @return org.jelik.types.Type
     */
    Type getGenericReturnType();

    /**
     * Returns type of this node i.e. expression a.foo.bar will return type of a
     *
     * @return org.jelik.types.Type
     */
    Type getType();

    /**
     * Returns generic type of this node i.e. expression a.foo.bar will return type of a
     *
     * @return org.jelik.types.Type
     */
    Type getGenericType();

    @NotNull
    default InferredTypeRef createInferredTypeRef() {
        return new InferredTypeRef(this);
    }

    default void setType(@NotNull Type type) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    default void setGenericType(@NotNull Type type) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }
}
