package org.jelik.compiler.data;

import org.jelik.parser.ast.functions.ConstructorTargetFunctionCallProvider;
import org.jelik.parser.ast.functions.providers.ExtTargetFunctionCallProvider;
import org.jelik.parser.ast.functions.providers.InstanceTargetFunctionCallProvider;
import org.jelik.parser.ast.functions.providers.StaticTargetFunctionCallProvider;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.types.FunctionType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

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

    boolean isConstructor();

    boolean isStatic();

    Type getOwner();

    List<Type> getExpectedTypeParameters();

    default boolean isExt() {
        return false;
    }

    default boolean isAbstract() {
        return false;
    }

    default String getArgumentsDescriptor() {
        return "(" + getParameterTypes().stream().map(Type::getInternalName).collect(Collectors.joining()) + ")";
    }

    default FunctionType getFunctionType() {
        return FunctionType.Companion.getFunctionType(getParameterTypes().size(), getReturnType());
    }

    default boolean isBuiltin() {
        return false;
    }

    default boolean isInterface() {
        return getOwner().isInterface();
    }

    default TargetFunctionCallProvider<?> getCallProvider() {
        if (isExt()) {
            return new ExtTargetFunctionCallProvider(this);
        } else if (isStatic()) {
            return new StaticTargetFunctionCallProvider(this);
        } else if (isConstructor()) {
            return new ConstructorTargetFunctionCallProvider(this);
        } else {
            return new InstanceTargetFunctionCallProvider(this);
        }
    }
}
