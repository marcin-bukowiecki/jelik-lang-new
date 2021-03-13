package org.jelik.parser.ast.types;

import org.jelik.compiler.common.TypeEnum;
import org.jelik.types.Type;

import java.lang.reflect.GenericArrayType;
import java.util.Collections;

public class JelikGenericArrayType extends Type {

    public JelikGenericArrayType(GenericArrayType genericArrayType) {
        super(genericArrayType.toString(),
                genericArrayType.toString(),
                TypeEnum.genericArray,
                Collections.singletonList(TypeUtils.createGenericType(genericArrayType.getGenericComponentType())),
                Collections.singletonList(TypeUtils.createGenericType(genericArrayType.getGenericComponentType()))
        );
    }
}
