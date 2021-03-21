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

import org.jelik.CompilationContext;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LongWrapperType extends IntegerWrapperType {

    public static final LongWrapperType INSTANCE = new LongWrapperType();

    private LongWrapperType() {
        super("Long", "java.lang.Long", TypeEnum.int64Wrapper);
    }

    @Override
    public Type getPrimitiveType() {
        return JVMLongType.INSTANCE;
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        switch (type.getTypeEnum()) {
            case float64:
            case float64Wrapper:
            case float32:
            case float32Wrapper:
            case int64:
            case int64Wrapper:
                return true;
            default:
                return getAssignableToTypes(compilationContext).contains(type);
        }
    }
}
