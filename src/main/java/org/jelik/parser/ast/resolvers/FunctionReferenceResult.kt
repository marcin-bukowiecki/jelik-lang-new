package org.jelik.parser.ast.resolvers

import org.jelik.CompilationContext
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.functions.FunctionCall
import org.jelik.parser.ast.functions.FunctionReferenceNode

/**
 * @author Marcin Bukowiecki
 */
class FunctionReferenceResult(val functions: List<MethodData>) : FindSymbolResult {

    override fun replaceNode(literalExpr: LiteralExpr): Expression {
        val functionReferenceNode = FunctionReferenceNode(literalExpr, functions)
        literalExpr.parent.replaceWith(literalExpr, functionReferenceNode)
        return functionReferenceNode
    }

    override fun findMethodData(caller: FunctionCall?, compilationContext: CompilationContext?): MutableList<MethodData> {
        return functions.toMutableList()
    }
}
