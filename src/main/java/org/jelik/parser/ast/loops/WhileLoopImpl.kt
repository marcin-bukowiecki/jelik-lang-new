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
import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.branching.WhileConditionExpression
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.TypedExpression
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.Token
import org.jelik.parser.token.keyword.WhileKeyword
import org.jelik.types.JVMVoidType

/**
 * Represents while expr do
 *              expr
 *            end
 *
 * @author Marcin Bukowiecki
 */
class WhileLoopImpl(
    private val whileKeyword: WhileKeyword,
    override var condition: WhileConditionExpression,
    private val left: Token,
    override var block: BasicBlockImpl,
    val right: Token
) : TypedExpression(), WhileLoop {

    override lateinit var loopStart: LabelNode

    override lateinit var loopEnd: LabelNode

    init {
        block.parent = this
        condition.parent = this

        nodeContext.type = JVMVoidType.INSTANCE
        nodeContext.genericType = JVMVoidType.INSTANCE
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitWhileLoop(this, compilationContext)
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        when {
            this.condition == oldNode -> {
                this.condition = newNode as WhileConditionExpression
                this.condition.parent = this
            }
            this.block == oldNode -> {
                this.block = newNode as BasicBlockImpl
                this.block.parent = this
            }
            else -> {
                throw IllegalArgumentException("Unsuuported expression to repalce: $newNode")
            }
        }
    }

    override fun getStartOffset(): Int {
        return whileKeyword.startOffset
    }

    override fun getEndOffset(): Int {
        return right.endOffset
    }

    fun getCondition(): Expression {
        return condition
    }

    override fun toString(): String {
        return "$whileKeyword $condition $left $block $right"
    }
}
