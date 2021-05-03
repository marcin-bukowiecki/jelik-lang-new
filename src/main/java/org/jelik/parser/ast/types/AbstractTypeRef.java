package org.jelik.parser.ast.types;

import org.jelik.parser.ast.functions.FunctionReferenceNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public abstract class AbstractTypeRef {

    public abstract Type getType();

    public abstract Type getGenericType();

    public abstract void setType(@NotNull Type type);

    public abstract void setGenericType(@NotNull Type type);

    public Optional<FunctionReferenceNode> getFunctionReference() {
        return Optional.empty();
    }

    public boolean isFunctionReference() {
        return false;
    }
}
