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

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ByteCodeArrayGetVisitor
import org.jelik.compiler.asm.visitor.StoreLocalByteCodeVisitor
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.locals.LocalVariable
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.types.InnerInferredTypeRef
import org.jelik.parser.ast.types.TypeRefWrapper
import org.jelik.types.JVMIntType
import org.objectweb.asm.Opcodes

/**
 * Next element collection for JVM primitive array type
 *
 * @author Marcin Bukowiecki
 */
class ArrayForEachASMProvider(forEachLoop: ForEachLoop) : CollectionForEachASMProvider(forEachLoop) {

    override fun nextElement(visit: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        mv.aload(forEachLoop.locals[2].index)
        mv.intLoad(forEachLoop.locals[0].index)
        forEachLoop.elementType().accept(ByteCodeArrayGetVisitor(mv), mv.compilationContext)
        forEachLoop.elementType().accept(StoreLocalByteCodeVisitor({ forEachLoop.locals[1] },mv), mv.compilationContext)
        mv.incr(forEachLoop.locals[0].index, 1)
    }

    override fun resolveTypeCast(compilationContext: CompilationContext) {
        val arrayLocalVariable = LocalVariable("\$arrayLocalVariable",
            TypeRefWrapper(forEachLoop.getIterExpression().genericReturnType), false)

        arrayLocalVariable.start = forEachLoop.locals[0].start
        arrayLocalVariable.end = forEachLoop.locals[0].end

        compilationContext
            .blockStack
            .last
            .blockContext
            .addLocal(arrayLocalVariable.name, arrayLocalVariable)

        (compilationContext.currentCompilationUnit() as FunctionDeclaration)
            .functionContext
            .addLocalVariable(arrayLocalVariable)
        forEachLoop.locals = forEachLoop.locals + listOf(arrayLocalVariable)
    }

    override fun newIterator(mv: MethodVisitorAdapter) {
        mv.pushInt(0)
        mv.intStore(forEachLoop.locals[0].index)
        mv.objectStore(forEachLoop.locals[2].index)
    }

    override fun hasNext(mv: MethodVisitorAdapter) {
        mv.intLoad(forEachLoop.locals[0].index)
        mv.aload(forEachLoop.locals[2].index)
        mv.visitArrayLength()
        mv.invokeStatic("jelik/lang/JelikUtils",
            "checkBounds",
            "(II)Z")
        mv.visitJumpInstruction(Opcodes.IFEQ, forEachLoop.loopEnd)
    }

    override fun createLocalVariables(): List<LocalVariable> {
        return listOf(
            LocalVariable(
            forEachLoop.getVarExpr().getName() + "ArrayIndex",
            TypeRefWrapper(JVMIntType.INSTANCE),
            false
            ),
            LocalVariable(
                forEachLoop.getVarExpr().getName(),
                InnerInferredTypeRef(forEachLoop.getIterExpression()),
                false
            ),
            LocalVariable(
                forEachLoop.getVarExpr().getName() + "ArrayRef",
                TypeRefWrapper(JVMIntType.INSTANCE),
                false
            )
        )
    }
}
