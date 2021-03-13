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

/**
 * @author Marcin Bukowiecki
 */
public class InterfaceType extends Type {

    private InterfaceType(final String name, final String canonicalName) {
        super(name, canonicalName, TypeEnum.interfaceT);
    }

    public static InterfaceType of(final String name, final String canonicalName) {
        return new InterfaceType(name, canonicalName);
    }

    public InterfaceType(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public boolean isInterface() {
        return true;
    }
}
