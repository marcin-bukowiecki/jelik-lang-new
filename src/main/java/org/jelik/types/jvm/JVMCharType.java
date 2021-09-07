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
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.types.JVMIntType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class JVMCharType extends JVMIntType {

    public static final JVMCharType INSTANCE = new JVMCharType();

    private JVMCharType() {
        super("Char", "char", TypeEnum.charT);
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        switch (type.typeEnum) {
            case charT:
            case int16:
            case int32:
            case int64:
            case float32:
            case float64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getInternalName() {
        return "C";
    }

    @Override
    public String getDescriptor() {
        return "C";
    }

    @Override
    public NumberType getWrapperType() {
        return CharWrapperType.INSTANCE;
    }
}
