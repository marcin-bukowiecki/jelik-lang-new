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
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.locals.LocalVariable
import org.jelik.types.JVMArrayType

/**
 * Class for getting next element from collection/array in for each loop [ForEachLoop]
 *
 * @author Marcin Bukowiecki
 */
interface ForEachASMProvider {

    fun nextElement(visit: ToByteCodeVisitor, mv: MethodVisitorAdapter)

    fun hasNext(mv: MethodVisitorAdapter)

    fun newIterator(mv: MethodVisitorAdapter)

    @Deprecated("use createLocalVariables on ForEachLoop")
    fun createLocalVariables(): List<LocalVariable>

    fun resolveTypeCast(compilationContext: CompilationContext) {

    }

    companion object {
        fun resolveProvider(forEachLoop: ForEachLoop,
                            compilationContext: CompilationContext
        ): ForEachASMProvider {
            val iterType = forEachLoop.getIterExpression().genericReturnType
            if (iterType.isCollection(compilationContext)) {
                if (iterType.typeVariables[0].isPrimitive) {
                    return PrimitiveCollectionForEachASMProvider(forEachLoop)
                }
                return CollectionForEachASMProvider(forEachLoop)
            } else if (iterType is JVMArrayType) {
                return ArrayForEachASMProvider(forEachLoop)
            } else {
                throw IllegalArgumentException("Expected java.util.Collection or JVM array got: $iterType")
            }
        }
    }
}
