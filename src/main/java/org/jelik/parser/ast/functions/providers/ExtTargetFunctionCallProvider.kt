package org.jelik.parser.ast.functions.providers

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.classes.ModuleDeclaration
import org.jelik.parser.ast.functions.ExtensionFunctionDeclaration
import org.jelik.parser.ast.functions.ExtensionFunctionDeclarationImpl
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.utils.ASTUtils

/**
 * Provider to call extension functions
 *
 * @author Marcin Bukowiecki
 */
open class ExtTargetFunctionCallProvider(methodData: MethodData): TargetFunctionCallProvider<MethodData>(methodData) {

    override fun getCodeGenProvider(): (FunctionCallExpr,
                                        ToByteCodeVisitor,
                                        MethodVisitorAdapter) -> Unit {

        return { functionCallExpr, byteCodeVisitor, currentMethodVisitor ->
            for (argument in functionCallExpr.argumentList.arguments) {
                argument.accept(byteCodeVisitor, currentMethodVisitor.compilationContext)
            }
            val targetFunctionCallProvider = functionCallExpr.targetFunctionCallProvider
            val targetFunctionCall = targetFunctionCallProvider.methodData as ExtensionFunctionDeclarationImpl
            val place= targetFunctionCall.parent as ModuleDeclaration
            currentMethodVisitor.invokeStatic(
                    place.type.internalName,
                    functionCallExpr.name,
                    targetFunctionCall.descriptor,
                    targetFunctionCall.returnType,
                    targetFunctionCall.parameterTypes,
                    targetFunctionCall.isInterface
            )
        }
    }
}
