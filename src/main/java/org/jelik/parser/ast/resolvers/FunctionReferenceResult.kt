package org.jelik.parser.ast.resolvers

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.functions.FunctionCall
import org.jelik.parser.ast.functions.FunctionReferenceNodeImpl

/**
 * @author Marcin Bukowiecki
 */
class FunctionReferenceResult(val functions: List<MethodData>) : FindSymbolResult {

    override fun replaceNode(literalExpr: LiteralExpr): Expression {
        val functionReferenceNode = FunctionReferenceNodeImpl(literalExpr, functions)
        literalExpr.parent.replaceWith(literalExpr, functionReferenceNode)
        return functionReferenceNode
    }

    override fun findMethodData(caller: FunctionCall?, compilationContext: CompilationContext?): MutableList<MethodData> {
        return functions.toMutableList()
    }
}
