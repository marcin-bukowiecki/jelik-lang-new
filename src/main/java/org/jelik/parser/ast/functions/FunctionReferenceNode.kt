package org.jelik.parser.ast.functions

import org.jelik.CompilationContext
import org.jelik.compiler.data.LambdaMethodData
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.expression.ExpressionReferencingType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type
import org.jelik.types.UnresolvedFunctionType

/**
 * @author Marcin Bukowiecki
 */
class FunctionReferenceNode(val literalExpr: LiteralExpr, var possibleFunctionsToCall: List<MethodData>) : ExpressionReferencingType() {

    lateinit var lambdaMethod: LambdaMethodData

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitFunctionReference(this, compilationContext)
    }

    override fun getType(): Type {
        return if (possibleFunctionsToCall.size == 1) {
            possibleFunctionsToCall[0].functionType
        } else {
            UnresolvedFunctionType(possibleFunctionsToCall)
        }
    }

    override fun getGenericReturnType(): Type {
        return type
    }

    fun setTargetFunction(methodData: MethodData) {
        this.possibleFunctionsToCall = listOf(methodData)
    }
}
