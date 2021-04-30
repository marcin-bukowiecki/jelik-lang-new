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

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.compiler.data.ClassData;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class JelikGenericType extends Type {

    public JelikGenericType(String name) {
        super(name, name, TypeEnum.objectT);
    }

    @Override
    public String forErrorMessage() {
        return getErasedCanonicalName();
    }

    public String getTypeSignature() {
        return "Ljava/lang/Object;";
    }

    public String getErasedCanonicalName() {
        return "java.lang.Object";
    }

    public String getErasedName() {
        return "Object";
    }

    @Override
    public String getInternalName() {
        return "java/lang/Object";
    }

    @Override
    public String getDescriptor() {
        return "Ljava/lang/Object;";
    }

    @Override
    public ClassData findClassData(@NotNull CompilationContext compilationContext) {
        return JVMObjectType.INSTANCE.findClassData(compilationContext);
    }

    @Override
    @NotNull
    public String getCanonicalName() {
        return getErasedCanonicalName();
    }

    @Override
    public Type deepGenericCopy() {
        return new JelikGenericType(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        var type = (Type) o;
        return name.equals(type.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
