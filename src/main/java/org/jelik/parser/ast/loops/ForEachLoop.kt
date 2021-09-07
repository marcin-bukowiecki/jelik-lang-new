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
import org.jelik.compiler.locals.LocalVariable
import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.expression.EmptyExpression
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.TypedExpression
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.types.InferredTypeRef
import org.jelik.parser.ast.types.InnerInferredTypeRef
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.Token
import org.jelik.parser.token.keyword.ForKeyword
import org.jelik.parser.token.keyword.InKeyword
import org.jelik.types.JVMVoidType
import org.jelik.types.Type

/**
 * Represents for expr in expr do
 *              expr
 *            end
 *
 * @author Marcin Bukowiecki
 */
class ForEachLoop(
    private val forKeyword: ForKeyword,
    private val varExpr: LoopVar,
    private val inKeyword: InKeyword,
    private var iterExpression: Expression,
    private val left: Token,
    private val block: BasicBlockImpl,
    private val right: Token
) : TypedExpression() {

    lateinit var loopStart: LabelNode

    lateinit var loopBodyStart: LabelNode

    lateinit var loopEnd: LabelNode

    var locals: List<LocalVariable> = listOf()

    var castToSite: List<Expression> = listOf(EmptyExpression.INSTANCE)

    init {
        varExpr.parent = this
        iterExpression.parent = this
        block.parent = this

        nodeContext.type = JVMVoidType.INSTANCE
        nodeContext.genericType = JVMVoidType.INSTANCE
    }

    fun getIterExpression() = iterExpression

    fun getVarExpr() = varExpr

    fun getBlock() = block

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitForEachLoop(this, compilationContext)
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        iterExpression = newNode
        iterExpression.parent = this
    }

    override fun getStartOffset(): Int {
        return forKeyword.startOffset
    }

    override fun getEndOffset(): Int {
        return right.endOffset
    }

    override fun toString(): String {
        return "$forKeyword $varExpr $inKeyword $iterExpression $left $block $right"
    }

    fun getForEachASMProvider(compilationContext: CompilationContext): ForEachASMProvider {
        return ForEachASMProvider.resolveProvider(this, compilationContext)
    }

    fun createLocalVariables(): List<LocalVariable> {
        return listOf(
            LocalVariable(
                getVarExpr().getName() + "Iterator",
                InferredTypeRef(getIterExpression()),
                false
            ),
            LocalVariable(
                getVarExpr().getName(),
                InnerInferredTypeRef(getIterExpression()),
                false
            )
        )
    }

    fun elementType(): Type {
        return iterExpression.genericReturnType.typeVariables[0]
    }
}
