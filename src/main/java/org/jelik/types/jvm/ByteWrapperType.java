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

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public final class ByteWrapperType extends IntegerWrapperType {

    public static final ByteWrapperType INSTANCE = new ByteWrapperType();

    private ByteWrapperType() {
        super("Byte", "java.lang.Byte", TypeEnum.int8Wrapper);
    }

    public static ByteWrapperType getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        switch (number.getTypeEnum()) {
            case int8:
            case int8Wrapper:
            case int16:
            case int16Wrapper:
            case int32:
            case int32Wrapper:
            case float64:
            case float64Wrapper:
            case float32:
            case float32Wrapper:
                return true;
            default:
                return getAssignableToTypes(compilationContext).contains(number);
        }
    }

    @Override
    public NumberType getWrapperType() {
        throw new UnsupportedOperationException("Byte wrapper is already a wrapper!!!");
    }
}
