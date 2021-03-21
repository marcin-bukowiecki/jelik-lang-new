package org.jelik.parser.ast.types;

import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public abstract class AbstractTypeRef {

    public abstract Type getType();

    public abstract Type getGenericType();

    public abstract void setType(Type type);

    public abstract void setGenericType(Type type);

    public boolean isFunctionReference() {
        return false;
    }
}
