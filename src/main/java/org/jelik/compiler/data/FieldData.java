package org.jelik.compiler.data;

import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public interface FieldData {
    String getName();

    Type getType();

    Type getGenericType();

    ClassData getOwner();

    int getModifiers();

    boolean isStatic();
}
