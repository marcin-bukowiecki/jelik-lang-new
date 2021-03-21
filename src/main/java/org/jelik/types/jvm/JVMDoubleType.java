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
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents JVM double type
 *
 * @author Marcin Bukowiecki
 */
public class JVMDoubleType extends NumberType {

    public static final JVMDoubleType INSTANCE = new JVMDoubleType();

    private JVMDoubleType() {
        super("double", "double", TypeEnum.float64);
    }

    @Override
    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public String getDescriptor() {
        return "D";
    }

    @Override
    public String getInternalName() {
        return "D";
    }

    @Override
    public boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        return isAssignableTo(number, compilationContext);
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        switch (type.getTypeEnum()) {
            case float64:
            case float64Wrapper:
            case objectT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public NumberType getWrapperType() {
        return DoubleWrapperType.INSTANCE;
    }
}
