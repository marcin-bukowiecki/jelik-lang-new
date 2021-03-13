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
import org.jelik.parser.ast.Expression;
import org.jelik.types.JVMIntType;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class IntegerWrapperType extends NumberType {

    public static final IntegerWrapperType INSTANCE = new IntegerWrapperType();

    private IntegerWrapperType() {
        super("Integer", "java.lang.Integer", TypeEnum.int32Wrapper);
    }

    protected IntegerWrapperType(String name, String canonicalName, TypeEnum typeEnum) {
        super(name, canonicalName, typeEnum);
    }

    @Override
    public boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        switch (number.getTypeEnum()) {
            case float64:
            case float64Wrapper:
            case float32:
            case float32Wrapper:
            case int64:
            case int64Wrapper:
            case int32:
            case int32Wrapper:
                return true;
            default:
                return getAssignableToTypes(compilationContext).contains(number);
        }
    }

    @Override
    public NumberType getWrapperType() {
        throw new UnsupportedOperationException("Integer wrapper is already a wrapper!!!");
    }

    @Override
    public boolean isWrapper() {
        return true;
    }

    @Override
    public Type getPrimitiveType() {
        return JVMIntType.INSTANCE;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public void castFrom(Expression expression, IntegerWrapperType type, CompilationContext compilationContext) {

    }
}
