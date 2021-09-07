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

package org.jelik.types.jvm;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class CharWrapperType extends NumberType {

    public static final CharWrapperType INSTANCE = new CharWrapperType();

    private CharWrapperType() {
        super("Character", "java.lang.Character", TypeEnum.characterWrapper);
    }

    @Override
    protected boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        return false;
    }

    @Override
    public NumberType getWrapperType() {
        throw new UnsupportedOperationException("Byte wrapper is already a wrapper!!!");
    }
}
