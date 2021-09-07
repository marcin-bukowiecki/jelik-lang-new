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

import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.types.Type;

public class DoubleWrapperType extends IntegerWrapperType {

    public static final DoubleWrapperType INSTANCE = new DoubleWrapperType();

    private DoubleWrapperType() {
        super("Double", "java.lang.Double", TypeEnum.float64Wrapper);
    }

    @Override
    public Type getPrimitiveType() {
        return JVMDoubleType.INSTANCE;
    }
}
