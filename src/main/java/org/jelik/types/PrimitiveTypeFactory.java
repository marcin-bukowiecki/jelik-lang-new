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

import org.jelik.types.jvm.JVMByteType;
import org.jelik.types.jvm.JVMCharType;
import org.jelik.types.jvm.JVMDoubleType;
import org.jelik.types.jvm.JVMFloatType;
import org.jelik.types.jvm.JVMLongType;
import org.jelik.types.jvm.JVMShortType;

/**
 * @author Marcin Bukowiecki
 */
public class PrimitiveTypeFactory {

    public static Type of(Class<?> clazz) {
        switch (clazz.getName()) {
            case "int":
                return JVMIntType.INSTANCE;
            case "short":
                return JVMShortType.INSTANCE;
            case "boolean":
                return JVMBooleanType.INSTANCE;
            case "byte":
                return JVMByteType.INSTANCE;
            case "char":
                return JVMCharType.INSTANCE;
            case "long":
                return JVMLongType.INSTANCE;
            case "float":
                return JVMFloatType.INSTANCE;
            case "double":
                return JVMDoubleType.INSTANCE;
            case "void":
                return JVMVoidType.INSTANCE;
            default:
                throw new RuntimeException("Could not resolve jvm symbol for class: " + clazz);
        }
    }

}
