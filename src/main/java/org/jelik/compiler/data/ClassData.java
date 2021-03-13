package org.jelik.compiler.data;

import org.jelik.CompilationContext;
import org.jelik.types.Type;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface ClassData {
    List<? extends MethodData> findByName(String name, CompilationContext compilationContext);

    List<FieldData> findFieldByName(String name);

    boolean hasSuperClass();

    List<Type> getInterfaceTypes();

    Type getParentType();

    Type getType();
}
