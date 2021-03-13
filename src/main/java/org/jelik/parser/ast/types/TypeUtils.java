package org.jelik.parser.ast.types;

import org.jelik.compiler.common.TypeEnum;
import org.jelik.types.Type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeUtils {

    public static Type createGenericType(java.lang.reflect.Type type) {
        if (type instanceof ParameterizedType) {
            return new Type(
                    ((ParameterizedType) type).getRawType().getTypeName(),
                    ((ParameterizedType) type).getRawType().getTypeName(),
                    TypeEnum.objectT,
                    Arrays.stream(((Class<?>) ((ParameterizedType) type).getRawType()).getTypeParameters()).map(TypeUtils::createGenericType).collect(Collectors.toList()),
                    Arrays.stream(((ParameterizedType) type).getActualTypeArguments()).map(TypeUtils::createGenericType).collect(Collectors.toList())
            );
        } else if (type instanceof TypeVariable) {
            return new JelikGenericType(((TypeVariable<?>) type).getName());
        } else if (type instanceof WildcardType) {
            return new JelikWildCardType(((WildcardType) type));
        } else if (type instanceof Class){
            return Type.of(((Class<?>) type));
        } else if (type instanceof GenericArrayType) {
            return new JelikGenericArrayType(((GenericArrayType) type));
        } else {
            throw new UnsupportedOperationException(type.toString());
        }
    }
}
