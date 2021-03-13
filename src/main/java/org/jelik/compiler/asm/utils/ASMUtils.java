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

package org.jelik.compiler.asm.utils;

import org.jelik.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

/**
 * @author Marcin Bukowiecki
 */
public class ASMUtils {

    /**
     * Checks if given JVM Class is a {@link FunctionalInterface}
     *
     * @param clazz given JVM class
     * @return true if given class is a {@link FunctionalInterface}, false otherwise
     */
    public static boolean isFunctionalInterface(Class<?> clazz) {
        return clazz.getAnnotation(FunctionalInterface.class) != null;
    }

    /**
     * Gets access modifiers from int value
     *
     * @param modifiers given int value
     * @return access modifier
     */
    public static int getAccessModifier(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return Opcodes.ACC_PUBLIC;
        }
        if (Modifier.isPrivate(modifiers)) {
            return Opcodes.ACC_PRIVATE;
        }
        if (Modifier.isProtected(modifiers)) {
            return Opcodes.ACC_PROTECTED;
        }
        return 0;
    }

    /**
     * Generates bytecode for boolean expression i.e. return foo == bar
     *
     * @param mv bytecode method visitor
     * @param expr given boolean expression
     * @param compilationContext given compilation context
     */
    public static void visitSingleExpr(@NotNull MethodVisitorAdapter mv,
                                 @NotNull AbstractLogicalOpExpr expr,
                                 @NotNull CompilationContext compilationContext) {

        mv.visitLabel(expr.trueLabelNode.getLabel());
        mv.pushInt(1);
        LabelNode label = compilationContext.createLabel("single-expr");
        mv.visitJumpInstruction(Opcodes.GOTO, label);
        mv.visitLabel(expr.falseLabelNode.getLabel());
        mv.pushInt(0);
        mv.visitLabel(label.getLabel());
    }
}
