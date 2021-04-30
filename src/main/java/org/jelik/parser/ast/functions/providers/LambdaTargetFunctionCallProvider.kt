package org.jelik.parser.ast.functions.providers

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.FunctionReferenceMethodData
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.parser.ast.resolvers.FunctionCallResolver

/**
 * @author Marcin Bukowiecki
 */
class LambdaTargetFunctionCallProvider(methodData: FunctionReferenceMethodData) :
    TargetFunctionCallProvider<FunctionReferenceMethodData>(methodData) {

    override fun resolveCallback(functionCallExpr: FunctionCallExpr, compilationContext: CompilationContext) {


        val resolvedCall = FunctionCallResolver()
            .resolveCall(
                compilationContext,
                functionCallExpr.argumentTypes,
                methodData.ref.getPossibleFunctionsToCall()
            )

        if (resolvedCall.isEmpty) {
            throw CompileException(
                "Could not resolve function reference call",
                functionCallExpr,
                compilationContext.currentModule
            )
        } else {
            val found = resolvedCall.get().methodData
            val returnType = found.returnType
            methodData.ref.setTargetFunction(found)
            methodData.ref.setFunctionReferenceMethod(methodData)
            methodData.returnType.accept(CastToVisitor(functionCallExpr, returnType), compilationContext)
        }
    }

    override fun getCodeGenProvider(): (FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter) -> Unit {

        return { functionCallExpr, byteCodeVisitor, currentMethodVisitor ->
            val targetFunctionCall = functionCallExpr.targetFunctionCallProvider
            methodData.callerProvider.invoke(currentMethodVisitor)
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.accept(byteCodeVisitor, currentMethodVisitor.compilationContext)
            }
            currentMethodVisitor.invokeInterface(
                functionCallExpr.owner.internalName,
                methodData.name,
                targetFunctionCall.methodData.descriptor,
                targetFunctionCall.methodData.returnType,
                targetFunctionCall.methodData.parameterTypes
            )
        }
    }
}
