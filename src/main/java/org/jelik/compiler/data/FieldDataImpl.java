package org.jelik.compiler.data;

import org.jelik.parser.ast.types.TypeUtils;
import org.jelik.types.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Marcin Bukowiecki
 */
public class FieldDataImpl implements FieldData {

    private final int modifiers;

    private final String name;

    private final Type type;

    private final Type genericType;

    private final ClassData owner;

    public FieldDataImpl(Field field, ClassData classData) {
        this.modifiers = field.getModifiers();
        this.name = field.getName();
        this.owner = classData;
        this.type = Type.of(field.getType());
        this.genericType = TypeUtils.createGenericType(field.getGenericType());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Type getGenericType() {
        return genericType;
    }

    @Override
    public ClassData getOwner() {
        return owner;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }
}
