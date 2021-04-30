package org.jelik.compiler.passes

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ReferenceExpression
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.locals.ValueDeclaration
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr
import org.jelik.parser.ast.resolvers.CastToVisitor

/**
 * Wraps primitive types with wrapper types
 *
 * @author Marcin Bukowiecki
 */
object PrimitiveTypeLifter : BasePass() {

    override fun visitFunctionCall(functionCallExpr: FunctionCallExpr, compilationContext: CompilationContext) {
        functionCallExpr.argumentList.accept(this, compilationContext)

        if (functionCallExpr.targetFunctionCallProvider.methodData.isExt) {
            return
        }

        if (functionCallExpr.parent is ReferenceExpression) {
            val refExpr = functionCallExpr.parent as ReferenceExpression
            val givenType = refExpr.reference.genericReturnType
            if (givenType.isPrimitive) {
                givenType.accept(CastToVisitor(refExpr.reference, givenType.wrapperType), compilationContext)
            }
        }
    }

    override fun visitNullSafeCall(nullSafeCall: NullSafeCallExpr, compilationContext: CompilationContext) {
        nullSafeCall.reference.accept(this, compilationContext)
        nullSafeCall.furtherExpression.accept(this, compilationContext)
        if (nullSafeCall.isLast()) {
            val type = nullSafeCall.furtherExpression.genericReturnType
            if (type.isPrimitive) {
                type.accept(CastToVisitor(nullSafeCall.furtherExpression, type.wrapperType), compilationContext)
            }
        }
    }
}
