package org.jelik.parser.ast.functions

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type
import org.jelik.types.UnresolvedFunctionType

/**
 * @author Marcin Bukowiecki
 */
class FunctionReferenceNodeImpl(
    val literalExpr: LiteralExpr,
    private var possibleFunctionsToCall: List<MethodData>
) : ASTNodeImpl(), Expression, FunctionReferenceNode {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitFunctionReference(this, compilationContext)
    }

    override fun getType(): Type {
        return if (possibleFunctionsToCall.size == 1) {
            possibleFunctionsToCall[0].functionType
        } else {
            UnresolvedFunctionType(possibleFunctionsToCall)
        }
    }

    override fun getGenericType(): Type {
        return type
    }

    override fun getReturnType(): Type {
        return type
    }

    override fun getGenericReturnType(): Type {
        return type
    }

    override fun setTargetFunction(methodData: MethodData) {
        this.possibleFunctionsToCall = listOf(methodData)
    }

    override fun getPossibleFunctionsToCall(): List<MethodData> {
        return possibleFunctionsToCall
    }
}
