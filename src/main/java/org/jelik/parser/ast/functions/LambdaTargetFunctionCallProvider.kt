package org.jelik.parser.ast.functions

import org.jelik.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.LambdaMethodData
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.parser.ast.resolvers.FunctionCallResolver

/**
 * @author Marcin Bukowiecki
 */
class LambdaTargetFunctionCallProvider(methodData: LambdaMethodData) : TargetFunctionCallProvider<LambdaMethodData>(methodData) {

    override fun resolveCallback(functionCallExpr: FunctionCallExpr, compilationContext: CompilationContext) {
        val resolvedCall = FunctionCallResolver().resolveCall(compilationContext, functionCallExpr.argumentTypes, methodData.ref.possibleFunctionsToCall)

        if (resolvedCall.isEmpty) {
            throw CompileException("Could not resolve function reference call", functionCallExpr, compilationContext.currentModule)
        } else {
            val found = resolvedCall.get().methodData
            val returnType = found.returnType
            methodData.ref.setTargetFunction(found)
            methodData.ref.lambdaMethod = methodData
            methodData.returnType.visit(CastToVisitor(functionCallExpr, returnType), compilationContext)
        }
    }

    override fun getCodeGenProvider(): (MethodVisitorAdapter,
                                        ToByteCodeVisitor,
                                        FunctionCallExpr,
                                        CompilationContext) -> Boolean {

        return { currentMethodVisitor, byteCodeVisitor, functionCallExpr, compilationContext ->
            val targetFunctionCall = functionCallExpr.targetFunctionCall
            methodData.callerProvider.invoke(currentMethodVisitor)
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.visit(byteCodeVisitor, compilationContext)
            }
            currentMethodVisitor.invokeInterface(
                    functionCallExpr.getOwner().internalName,
                    methodData.name,
                    targetFunctionCall.methodData.descriptor,
                    targetFunctionCall.methodData.returnType,
                    targetFunctionCall.methodData.parameterTypes
            )
            true
        }
    }
}
