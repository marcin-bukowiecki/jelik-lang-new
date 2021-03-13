package org.jelik.parser.ast.types;

import org.jelik.types.Type;

public abstract class TypeRef {

    public abstract Type getType();

    public abstract Type getGenericType();

    public abstract void setType(Type type);

    public abstract void setGenericType(Type type);
}
