package org.jelik.compiler.passes

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ReferenceExpression
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.locals.ValueDeclaration
import org.jelik.parser.ast.locals.ValueOrVariableDeclaration
import org.jelik.parser.ast.locals.VariableDeclaration
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr
import org.jelik.parser.ast.operators.DefaultValueExpr
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.parser.ast.types.UndefinedTypeNode

/**
 * Wraps primitive types with wrapper types
 *
 * @author Marcin Bukowiecki
 */
object PrimitiveTypeLifter : BasePass() {

    override fun visitFunctionCall(functionCallExpr: FunctionCallExpr, compilationContext: CompilationContext) {
        functionCallExpr.argumentList.accept(this, compilationContext)

        if (functionCallExpr.targetFunctionCallProvider.isExt()) {
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
        if (nullSafeCall.referenceGenericType()?.isPrimitive == true) {
            val wrapperType = nullSafeCall.referenceGenericType()!!.wrapperType
            nullSafeCall.referenceGenericType()!!.accept(
                CastToVisitor(
                    nullSafeCall.extractNullSubject()!!,
                    wrapperType
                ), compilationContext)
        }
        nullSafeCall.furtherExpression.accept(this, compilationContext)
        if (nullSafeCall.isLast()) {
            val type = nullSafeCall.furtherExpression.genericReturnType
            if (type.isPrimitive) {
                if (nullSafeCall.parent is DefaultValueExpr) {
                    val genericReturnType = (nullSafeCall.parent as DefaultValueExpr).right.genericReturnType
                    type.accept(CastToVisitor(nullSafeCall.furtherExpression, genericReturnType), compilationContext)
                } else {
                    type.accept(CastToVisitor(nullSafeCall.furtherExpression, type.wrapperType), compilationContext)
                }
            }
        }
    }

    override fun visitValueDeclaration(valueDeclaration: ValueDeclaration,
                                       compilationContext: CompilationContext
    ) {
        visitWithLocalVariable(valueDeclaration as ValueOrVariableDeclaration, compilationContext)
    }

    override fun visitVariableDeclaration(
        variableDeclaration: VariableDeclaration,
        compilationContext: CompilationContext
    ) {
        visitWithLocalVariable(variableDeclaration as ValueOrVariableDeclaration, compilationContext)
    }

    override fun visitWithLocalVariable(
        valueOrVariableDeclaration: ValueOrVariableDeclaration,
        compilationContext: CompilationContext
    ) {
        valueOrVariableDeclaration.expression.accept(this, compilationContext)

        if (valueOrVariableDeclaration.typeNode != UndefinedTypeNode.UNDEFINED_TYPE_NODE) {

            if (valueOrVariableDeclaration.typeNode.type.isPrimitive &&
                valueOrVariableDeclaration.expression.genericReturnType.isWrapper) {

                valueOrVariableDeclaration
                    .expression
                    .genericReturnType
                    .accept(CastToVisitor(
                        valueOrVariableDeclaration.expression,
                        valueOrVariableDeclaration.typeNode.type
                    ), compilationContext)
            }
        }
    }
}
