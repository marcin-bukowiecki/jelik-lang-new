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
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.numbers.Int32ToInt64Node;
import org.jelik.types.JVMIntType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents JVM long type
 *
 * @author Marcin Bukowiecki
 */
public class JVMLongType extends NumberType {

    public static final JVMLongType INSTANCE = new JVMLongType();

    public JVMLongType() {
        super("Long", "Long", TypeEnum.int64);
    }

    @Override
    public boolean isNumberAssignableTo(Type number, CompilationContext compilationContext) {
        return isAssignableTo(number, compilationContext);
    }

    @Override
    public String getDescriptor() {
        return "J";
    }

    @Override
    public String getInternalName() {
        return "J";
    }

    @Override
    public boolean isAssignableTo(@NotNull Type number, @NotNull CompilationContext compilationContext) {
        switch (number.getTypeEnum()) {
            case int64:
            case int64Wrapper:
            case float64:
            case float64Wrapper:
            case float32:
            case float32Wrapper:
            case objectT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public boolean isLong() {
        return true;
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public NumberType getWrapperType() {
        return LongWrapperType.INSTANCE;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public void castFrom(Expression expression, JVMIntType type, CompilationContext compilationContext) {
        expression.getParent().replaceWith(expression, new Int32ToInt64Node(expression));
    }
}
