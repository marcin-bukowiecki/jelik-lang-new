package org.jelik.parser.ast.functions.providers

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.functions.FunctionCallExpr

/**
 * @author Marcin Bukowiecki
 */
class InstanceTargetFunctionCallProvider(methodData: MethodData) : TargetFunctionCallProvider<MethodData>(methodData) {

    override fun getCodeGenProvider(): (FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter) -> Unit {
        return { functionCallExpr, byteCodeVisitor, currentMethodVisitor ->
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.accept(byteCodeVisitor, currentMethodVisitor.compilationContext)
            }
            val targetFunctionCall = functionCallExpr.targetFunctionCallProvider
            var owner = functionCallExpr.owner
            if (owner.isPrimitive) {
               owner = owner.wrapperType
            }

            currentMethodVisitor.invokeInstance(
                    owner.internalName,
                    functionCallExpr.name,
                    targetFunctionCall.methodData.descriptor,
                    targetFunctionCall.methodData.returnType,
                    targetFunctionCall.methodData.parameterTypes,
                    functionCallExpr.owner.isInterface
            )
        }
    }
}
