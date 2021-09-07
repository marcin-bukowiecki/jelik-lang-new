package org.jelik.compiler.types

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.locals.LocalVariable
import org.jelik.compiler.utils.Stateless
import org.jelik.parser.ast.functions.FunctionCall
import org.jelik.parser.ast.functions.FunctionReferenceNode
import org.jelik.parser.ast.resolvers.FunctionCallResolver
import org.jelik.parser.ast.types.InferredTypeRef
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
@Stateless
object TypeInferenceWalker : AstVisitor() {

    fun resolveTargetCallForFunctionReference(lv: LocalVariable,
                                              callExpr: FunctionCall,
                                              compilationContext: CompilationContext
    ): Boolean {

        val argumentTypes = callExpr.argumentTypes
        val functionReferenceNode = (lv.typeRef as? InferredTypeRef)?.ref as? FunctionReferenceNode ?: return false

        return FunctionCallResolver()
            .resolveCall(compilationContext, argumentTypes, functionReferenceNode.getPossibleFunctionsToCall())
            .map { result ->
                functionReferenceNode.setTargetFunction(result.methodData)
                true
            }.orElse(false)
    }
}
