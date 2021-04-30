package org.jelik.parser.ast.functions

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider

/**
 * @author Marcin Bukowiecki
 */
class ConstructorTargetFunctionCallProvider(methodData: MethodData)
    : TargetFunctionCallProvider<MethodData>(methodData) {

    override fun getCodeGenProvider(): (FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter) -> Unit {

        return { functionCallExpr, byteCodeVisitor, currentMethodVisitor ->
            val targetFunctionCall = functionCallExpr.targetFunctionCall
            currentMethodVisitor.callNew(targetFunctionCall.methodData.owner)
            currentMethodVisitor.dup()
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.accept(byteCodeVisitor, currentMethodVisitor.compilationContext)
            }
            currentMethodVisitor.invokeSpecial(
                    functionCallExpr.getOwner().internalName,
                    "<init>",
                    targetFunctionCall.methodData.descriptor
            )
        }
    }
}
