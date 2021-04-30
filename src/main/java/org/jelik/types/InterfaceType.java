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

package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class InterfaceType extends Type {

    private InterfaceType(final String name, final String canonicalName) {
        super(name, canonicalName, TypeEnum.interfaceT);
    }

    public InterfaceType(String name,
                         String canonicalName,
                         TypeEnum typeEnum,
                         List<Type> typeParameters,
                         List<Type> typeVariables) {
        super(name, canonicalName, typeEnum, typeParameters, typeVariables);
    }

    public static InterfaceType of(final String name, final String canonicalName) {
        return new InterfaceType(name, canonicalName);
    }

    @Override
    public Type deepGenericCopy() {
        return new InterfaceType(
                name,
                canonicalName,
                typeEnum,
                typeVariables
                        .stream()
                        .map(Type::deepGenericCopy)
                        .collect(Collectors.toList()),
                typeParameters
                        .stream()
                        .map(Type::deepGenericCopy)
                        .collect(Collectors.toList())
        );
    }

    public InterfaceType(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public boolean isInterface() {
        return true;
    }

    @Override
    public String toString() {
        return canonicalName;
    }
}
