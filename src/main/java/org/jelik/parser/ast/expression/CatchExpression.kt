package org.jelik.parser.ast.expression

import org.jelik.CompilationContext
import org.jelik.parser.ast.BasicBlock
import org.jelik.parser.ast.functions.FunctionParameterList
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.CatchKeyword

/**
 * Expression representing catch expression
 *
 * @author Marcin Bukowiecki
 */
class CatchExpression(private val catchKeyword: CatchKeyword,
                      val args: FunctionParameterList,
                      var block: BasicBlock): ExpressionReferencingType() {

    lateinit var startLabel: LabelNode

    var innerLabel: LabelNode? = null

    lateinit var endLabel: LabelNode

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun toString(): String {
        return "$catchKeyword $args { $block } ${furtherExpressionOpt.map { expr -> expr.toString() }.orElse("")}"
    }
}
