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
import org.jelik.types.JVMIntType;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class JVMByteType extends JVMIntType {

    public static final JVMByteType INSTANCE = new JVMByteType();

    private JVMByteType() {
        super("Byte", "byte", TypeEnum.int8);
    }

    @Override
    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public boolean isAssignableTo(Type type, CompilationContext compilationContext) {
        switch (type.typeEnum) {
            case charT:
            case int8:
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
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public String getInternalName() {
        return "B";
    }

    @Override
    public String getDescriptor() {
        return "B";
    }

    @Override
    public NumberType getWrapperType() {
        return ByteWrapperType.INSTANCE;
    }
}