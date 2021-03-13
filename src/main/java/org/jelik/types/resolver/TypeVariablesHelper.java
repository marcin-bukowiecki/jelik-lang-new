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

package org.jelik.types.resolver;

import org.jelik.types.JelikTypeVariable;
import org.jelik.types.Type;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used only for already loaded classes
 */
public class TypeVariablesHelper {

    public static List<Type> resolveTypeVariables(Class<?> clazz) {
        TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
        List<Type> result = new ArrayList<>(typeParameters.length);

        for (TypeVariable<? extends Class<?>> typeParameter : typeParameters) {
            result.add(new JelikTypeVariable(typeParameter.getName()));
        }
        return result;
    }
}
