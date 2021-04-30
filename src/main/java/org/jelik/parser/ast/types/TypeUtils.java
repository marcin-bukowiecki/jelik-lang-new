/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.parser.ast.types;

import org.jelik.compiler.common.TypeEnum;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.functions.ExtensionFunctionDeclaration;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.types.InterfaceType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class TypeUtils {

    public static @Nullable Type getThisType(@NotNull ASTNode node) {
        var functionDeclaration = ASTUtils.getFunctionDeclaration(node);
        if (functionDeclaration == null) {
            var classDeclaration = ASTUtils.getClassDeclaration(node);
            if (classDeclaration == null) return null;
            return classDeclaration.getType();
        }
        if (functionDeclaration instanceof ExtensionFunctionDeclaration) {
            return ((ExtensionFunctionDeclaration) functionDeclaration).getExtOwner();
        }
        return functionDeclaration.getOwner();
    }

    public static Type createGenericType(java.lang.reflect.Type type, Class<?> returnTypeClass) {
        if (returnTypeClass.isInterface()) {
            return createGenericType(type, () -> new InterfaceType(
                    ((ParameterizedType) type).getRawType().getTypeName(),
                    ((ParameterizedType) type).getRawType().getTypeName(),
                    TypeEnum.objectT,
                    Arrays.stream(((Class<?>) ((ParameterizedType) type).getRawType()).getTypeParameters())
                            .map(TypeUtils::createGenericType)
                            .collect(Collectors.toList()),
                    Arrays.stream(((ParameterizedType) type).getActualTypeArguments())
                            .map(TypeUtils::createGenericType)
                            .collect(Collectors.toList())
            ));
        } else {
            return createGenericType(type);
        }
    }

    public static Type createGenericType(java.lang.reflect.Type type, Supplier<Type> typeProvider) {
        if (type instanceof ParameterizedType) {
            return typeProvider.get();
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

    public static Type createGenericType(java.lang.reflect.Type type) {
        return createGenericType(type, () -> new Type(
                ((ParameterizedType) type).getRawType().getTypeName(),
                ((ParameterizedType) type).getRawType().getTypeName(),
                TypeEnum.objectT,
                Arrays.stream(((Class<?>) ((ParameterizedType) type).getRawType()).getTypeParameters())
                        .map(TypeUtils::createGenericType)
                        .collect(Collectors.toList()),
                Arrays.stream(((ParameterizedType) type).getActualTypeArguments())
                        .map(TypeUtils::createGenericType)
                        .collect(Collectors.toList())
        ));
    }
}
