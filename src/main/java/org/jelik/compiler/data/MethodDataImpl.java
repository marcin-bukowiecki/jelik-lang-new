package org.jelik.compiler.data;

import org.jelik.parser.ast.types.TypeUtils;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class MethodDataImpl implements MethodData {

    private final int modifiers;

    private final String name;

    private final List<Type> parameterTypes;

    private final List<Type> genericParameterTypes;

    private final Type returnType;

    private final Type genericReturnType;

    private final ClassData owner;

    public MethodDataImpl(Method method, ClassData owner) {
        this.modifiers = method.getModifiers();
        this.name = method.getName();
        this.owner = owner;
        this.parameterTypes = Arrays.stream(method.getParameterTypes()).map(Type::of).collect(Collectors.toList());
        this.genericParameterTypes = Arrays.stream(method.getGenericParameterTypes()).map(TypeUtils::createGenericType).collect(Collectors.toList());
        this.returnType = Type.of(method.getReturnType());
        this.genericReturnType = TypeUtils.createGenericType(method.getGenericReturnType());
    }

    public MethodDataImpl(Constructor<?> constructor, ClassData owner) {
        this.modifiers = constructor.getModifiers();
        this.name = "<init>";
        this.owner = owner;
        this.parameterTypes = Arrays.stream(constructor.getParameterTypes()).map(Type::of).collect(Collectors.toList());
        this.genericParameterTypes = Arrays.stream(constructor.getGenericParameterTypes()).map(TypeUtils::createGenericType).collect(Collectors.toList());
        this.returnType = owner.getType();
        this.genericReturnType = owner.getType();
    }

    public MethodDataImpl(int modifiers,
                          @NotNull String name,
                          @NotNull List<Type> parameterTypes,
                          @NotNull List<Type> genericParameterTypes,
                          @NotNull Type returnType,
                          @NotNull Type genericReturnType,
                          @NotNull ClassData owner) {
        this.modifiers = modifiers;
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.genericParameterTypes = genericParameterTypes;
        this.returnType = returnType;
        this.genericReturnType = genericReturnType;
        this.owner = owner;
    }

    public int getModifiers() {
        return modifiers;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public List<Type> getGenericParameterTypes() {
        return genericParameterTypes;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public Type getGenericReturnType() {
        return genericReturnType;
    }

    @Override
    public String getDescriptor() {
        if (isConstructor()) {
            return "(" + parameterTypes.stream().map(Type::getDescriptor).collect(Collectors.joining()) + ")V";
        }
        return "(" + parameterTypes.stream().map(Type::getDescriptor).collect(Collectors.joining()) + ")" + returnType.getDescriptor();
    }

    public boolean isConstructor() {
        return name.equals("<init>");
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    @Override
    public Type getOwner() {
        return owner.getType();
    }

    @Override
    public String toString() {
        return this.name + getDescriptor();
    }
}
