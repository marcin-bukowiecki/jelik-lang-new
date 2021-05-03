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

import org.apache.commons.lang3.mutable.MutableInt;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.ClassWriterAdapter;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.LambdaDeclaration;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr;
import org.jelik.parser.ast.utils.ASTDataKey;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class ASMUtils {

    public static final String OBJECT_NO_ARGS = "()Ljava/lang/Object;";

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

    public static ClassWriterAdapter createClassWithDefaultConstructor(final String canonicalName) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V11,
                Opcodes.ACC_PUBLIC,
                canonicalName.replace('.', '/'),
                "",
                "java/lang/Object",
                null);
        createDefaultConstructor(classWriter);
        return new ClassWriterAdapter(classWriter);
    }

    public static void createDefaultConstructor(final ClassWriter classWriter) {
        var constructor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                "",
                null);
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
                false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
    }

    public static String generateLambdaName(final LambdaDeclaration lambdaDeclaration) {
        final String name = lambdaDeclaration.getOwner().getName();
        final ClassDeclaration classDeclaration = ASTUtils.getClassDeclaration(lambdaDeclaration);
        Objects.requireNonNull(classDeclaration);
        final MutableInt mutableInt = classDeclaration.getData(ASTDataKey.LAMBDA_ID_COUNTER);
        if (mutableInt == null) {
            classDeclaration.putData(ASTDataKey.LAMBDA_ID_COUNTER, new MutableInt(0));
            return name + "$0";
        } else {
            return name + mutableInt.incrementAndGet();
        }
    }
}
