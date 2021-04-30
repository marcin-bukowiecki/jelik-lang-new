package org.jelik.parser.ast.functions.providers

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.functions.FunctionCallExpr

/**
 * Provider to call static methods
 *
 * @author Marcin Bukowiecki
 */
open class StaticTargetFunctionCallProvider(methodData: MethodData)
    : TargetFunctionCallProvider<MethodData>(methodData) {

    override fun getCodeGenProvider(): (FunctionCallExpr,
                                        ToByteCodeVisitor,
                                        MethodVisitorAdapter) -> Unit {

        return { functionCallExpr, byteCodeVisitor, currentMethodVisitor ->
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.accept(byteCodeVisitor, currentMethodVisitor.compilationContext)
            }
            val targetFunctionCall = functionCallExpr.targetFunctionCallProvider
            currentMethodVisitor.invokeStatic(
                    functionCallExpr.owner.internalName,
                    functionCallExpr.name,
                    targetFunctionCall.methodData.descriptor,
                    targetFunctionCall.methodData.returnType,
                    targetFunctionCall.methodData.parameterTypes,
                    targetFunctionCall.methodData.isInterface
            )
        }
    }
}
