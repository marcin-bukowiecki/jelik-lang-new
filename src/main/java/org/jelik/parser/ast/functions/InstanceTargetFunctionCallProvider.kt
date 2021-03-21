package org.jelik.parser.ast.functions

import org.jelik.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.MethodData

/**
 * @author Marcin Bukowiecki
 */
class InstanceTargetFunctionCallProvider(methodData: MethodData) : TargetFunctionCallProvider<MethodData>(methodData) {

    override fun getCodeGenProvider(): (MethodVisitorAdapter, ToByteCodeVisitor, FunctionCallExpr, CompilationContext) -> Boolean {
        return { currentMethodVisitor, byteCodeVisitor, functionCallExpr, compilationContext ->
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.visit(byteCodeVisitor, compilationContext)
            }
            val targetFunctionCall = functionCallExpr.targetFunctionCall
            currentMethodVisitor.invokeInstance(
                    functionCallExpr.getOwner().internalName,
                    functionCallExpr.name,
                    targetFunctionCall.methodData.descriptor,
                    targetFunctionCall.methodData.returnType,
                    targetFunctionCall.methodData.parameterTypes,
                    targetFunctionCall.methodData.isInterface
            )
            true
        }
    }
}
