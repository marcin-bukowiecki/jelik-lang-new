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

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class JVMShortType extends NumberType {

    public static final JVMShortType INSTANCE = new JVMShortType();

    private JVMShortType() {
        super("Short", "short", TypeEnum.int16);
    }

    @Override
    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    protected boolean isNumberAssignableTo(Type type, CompilationContext compilationContext) {
        Objects.requireNonNull(type);

        if (type.getTypeEnum() == null) {
            return false;
        }

        switch (type.getTypeEnum()) {
            case int64:
            case int64Wrapper:
            case int32:
            case int32Wrapper:
            case float32:
            case float32Wrapper:
            case float64:
            case float64Wrapper:
            case objectT:
            case int16:
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
    public String getInternalName() {
        return "S";
    }

    @Override
    public String getDescriptor() {
        return "S";
    }

    @Override
    public NumberType getWrapperType() {
        return ShortWrapperType.INSTANCE;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }
}
