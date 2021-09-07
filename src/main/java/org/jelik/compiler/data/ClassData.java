package org.jelik.compiler.data;

import org.jelik.compiler.CompilationContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface ClassData {

    @NotNull
    List<? extends MethodData> findByName(String name, CompilationContext compilationContext);

    List<FieldData> findFieldByName(String name);

    boolean hasSuperClass();

    List<Type> getInterfaceTypes();

    List<MethodData> getMethodScope();

    Type getParentType();

    Type getType();

    Collection<? extends MethodData> getConstructorScope();

    boolean isInterface();

    boolean isAbstract();
}
