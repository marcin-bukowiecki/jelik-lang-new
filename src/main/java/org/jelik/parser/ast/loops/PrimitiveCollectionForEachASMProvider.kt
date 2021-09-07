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

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.utils.ASMUtils
import org.jelik.compiler.asm.visitor.StoreLocalByteCodeVisitor
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.parser.ast.casts.CastObjectToObjectNode
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.parser.ast.utils.ASTReplaceCaptor
import org.jelik.types.JVMObjectType
import org.jelik.types.Type
import org.jelik.types.jvm.IntegerWrapperType

/**
 * Next element collection for [java.util.Collection] type
 *
 * @author Marcin Bukowiecki
 */
class PrimitiveCollectionForEachASMProvider(forEachLoop: ForEachLoop) : CollectionForEachASMProvider(forEachLoop) {

    override fun nextElement(visit: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        mv.aload(forEachLoop.locals[0].index)
        mv.invokeInterface(Type.of(MutableIterator::class.java).internalName,
                "next",
                ASMUtils.OBJECT_NO_ARGS,
                JVMObjectType.INSTANCE, emptyList())

        forEachLoop.castToSite.forEach { n-> n.accept(visit, mv.compilationContext) }

        val storeLocalByteCodeVisitor = StoreLocalByteCodeVisitor(forEachLoop.locals[1], mv)
        val target: Type = forEachLoop.locals[1].type
        target.accept(storeLocalByteCodeVisitor, mv.compilationContext)
    }

    override fun resolveTypeCast(compilationContext: CompilationContext) {
        val collectionType = forEachLoop.getIterExpression().genericReturnType
        val target: Type = forEachLoop.locals[1].type
        val captor = ASTReplaceCaptor(forEachLoop.getIterExpression())
        collectionType.typeVariables[0].wrapperType.accept(
            CastToVisitor(captor, target),
            compilationContext
        )
        forEachLoop.castToSite = listOf(
            CastObjectToObjectNode(JVMObjectType.INSTANCE, IntegerWrapperType.INSTANCE),
            captor.captured!!
        )
    }
}
