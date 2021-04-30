package org.jelik.compiler.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.data.FunctionReferenceMethodData
import org.jelik.compiler.locals.LocalVariable
import org.jelik.parser.ast.functions.FunctionCall
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.functions.FunctionReferenceNode
import org.jelik.parser.ast.resolvers.FunctionCallResolver
import org.jelik.parser.ast.types.InferredTypeRef

/**
 * @author Marcin Bukowiecki
 */
object TypeInferenceWalker {

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

    fun inferArgumentsToLambda(methodData: FunctionReferenceMethodData,
                               functionCallExpr: FunctionCallExpr) {

        val argumentTypes = functionCallExpr.argumentTypes

    }
}
