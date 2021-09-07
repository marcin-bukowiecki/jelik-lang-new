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
import org.jelik.types.JVMBooleanType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class BooleanWrapperType extends NumberType {

    public static final BooleanWrapperType INSTANCE = new BooleanWrapperType();

    private BooleanWrapperType() {
        super("Boolean", "java.lang.Boolean", TypeEnum.booleanWrapper);
    }

    protected BooleanWrapperType(String name, String canonicalName, TypeEnum typeEnum) {
        super(name, canonicalName, typeEnum);
    }

    @Override
    protected boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        return false;
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        switch (type.typeEnum) {
            case booleanT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public NumberType getWrapperType() {
        throw new UnsupportedOperationException("Boolean wrapper is already a wrapper!!!");
    }

    @Override
    public boolean isWrapper() {
        return true;
    }

    @Override
    public Type getPrimitiveType() {
        return JVMBooleanType.INSTANCE;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }
}
