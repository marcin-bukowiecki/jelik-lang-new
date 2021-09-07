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
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public abstract class NumberType extends Type {

    protected NumberType(String name, String canonicalName, TypeEnum typeEnum) {
        super(name, canonicalName, typeEnum);
    }

    protected abstract boolean isNumberAssignableTo(Type number, CompilationContext compilationContext);

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isCollection(CompilationContext compilationContext) {
        return false;
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        if (type instanceof JVMObjectType) {
            return true;
        }
        if (type instanceof NumberType) {
            return isNumberAssignableTo(type, compilationContext);
        }
        return super.isAssignableTo(type, compilationContext);
    }

    public abstract NumberType getWrapperType();

    @Override
    public int hashCode() {
        return canonicalName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return canonicalName.equals(type.getCanonicalName());
    }

    public boolean isWrapper() {
        return false;
    }

    public Type getPrimitiveType() {
        return this;
    }
}
