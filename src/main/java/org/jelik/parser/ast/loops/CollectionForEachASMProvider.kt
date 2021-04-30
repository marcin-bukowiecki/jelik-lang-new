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

package org.jelik.parser.ast.loops

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.utils.ASMUtils
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.locals.LocalVariable
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.types.InferredTypeRef
import org.jelik.parser.ast.types.InnerInferredTypeRef
import org.jelik.types.JVMObjectType
import org.jelik.types.Type
import org.objectweb.asm.Opcodes

/**
 * Next element collection for [java.util.Collection] type
 *
 * @author Marcin Bukowiecki
 */
open class CollectionForEachASMProvider(protected val forEachLoop: ForEachLoop) : ForEachASMProvider {

    override fun nextElement(visit: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        mv.aload(forEachLoop.locals[0].index)
        mv.invokeInterface(Type.of(MutableIterator::class.java).internalName,
            "next",
            ASMUtils.OBJECT_NO_ARGS,
            JVMObjectType.INSTANCE, emptyList())
        mv.objectStore(forEachLoop.locals[1].index)
    }

    override fun hasNext(mv: MethodVisitorAdapter) {
        mv.aload(forEachLoop.locals[0].index)
        mv.invokeInterface(Type.of(MutableIterator::class.java).internalName,
                "hasNext",
                "()Z",
                JVMObjectType.INSTANCE, emptyList())
        mv.visitJumpInstruction(Opcodes.IFEQ, forEachLoop.loopEnd)
    }

    override fun newIterator(mv: MethodVisitorAdapter) {
        val iterExpression: Expression = forEachLoop.getIterExpression()
        mv.invokeInstance(iterExpression.genericReturnType.internalName,
                "iterator",
                "()Ljava/util/Iterator;",
                Type.of(MutableIterator::class.java), emptyList(),
                iterExpression.genericReturnType.isInterface)
        mv.objectStore(forEachLoop.locals[0].index)
    }

    override fun createLocalVariables(): List<LocalVariable> {
        return listOf(LocalVariable(
            forEachLoop.getVarExpr().getName() + "Iterator",
            InferredTypeRef(forEachLoop.getIterExpression()),
            false),
            LocalVariable(
                forEachLoop.getVarExpr().getName(),
                InnerInferredTypeRef(forEachLoop.getIterExpression()),
                false
            )
        )
    }
}
