package org.jelik.compiler.data

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.parser.ast.functions.FunctionReferenceNode
import org.jelik.parser.ast.functions.LambdaDeclarationExpression
import org.jelik.parser.ast.functions.providers.FunctionReferenceTargetFunctionCallProvider
import org.jelik.parser.ast.functions.providers.LambdaTargetFunctionCallProvider
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider
import org.jelik.types.Type
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class FunctionReferenceMethodData(val ref: FunctionReferenceNode,
                                  val callerProvider: (MethodVisitorAdapter) -> Unit,
                                  private val methodData: MethodData) : MethodData {

    override fun getName(): String {
        return methodData.name
    }

    override fun getDescriptor(): String {
        return methodData.descriptor
    }

    override fun getParameterTypes(): MutableList<Type> {
        return methodData.parameterTypes
    }

    override fun getReturnType(): Type {
        return methodData.returnType
    }

    override fun getGenericReturnType(): Type {
        return methodData.genericReturnType
    }

    override fun getGenericParameterTypes(): MutableList<Type> {
        return methodData.genericParameterTypes
    }

    override fun isConstructor(): Boolean {
        return false
    }

    override fun isStatic(): Boolean {
        return false
    }

    override fun getOwner(): Type {
        return methodData.owner
    }

    override fun getExpectedTypeParameters(): MutableList<Type> {
        return Collections.emptyList()
    }

    override fun getCallProvider(): TargetFunctionCallProvider<FunctionReferenceMethodData> {
        if (ref is LambdaDeclarationExpression) return LambdaTargetFunctionCallProvider(this)
        return FunctionReferenceTargetFunctionCallProvider(this)
    }
}
