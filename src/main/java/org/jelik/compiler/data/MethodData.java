package org.jelik.compiler.data;

import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface MethodData {

    @NotNull
    String getName();

    String getDescriptor();

    List<Type> getParameterTypes();

    Type getReturnType();

    Type getGenericReturnType();

    List<Type> getGenericParameterTypes();

    boolean isStatic();

    Type getOwner();

    default boolean isBuiltin() {
        return false;
    }

    default boolean isInterface() {
        return getOwner().isInterface();
    }
}
