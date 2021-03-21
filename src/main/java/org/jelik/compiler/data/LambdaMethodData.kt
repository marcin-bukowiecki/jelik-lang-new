package org.jelik.compiler.data

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.parser.ast.functions.FunctionReferenceNode
import org.jelik.parser.ast.functions.LambdaTargetFunctionCallProvider
import org.jelik.parser.ast.functions.TargetFunctionCallProvider
import org.jelik.types.Type
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class LambdaMethodData(val ref: FunctionReferenceNode,
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

    override fun getCallProvider(): TargetFunctionCallProvider<LambdaMethodData> {
        return LambdaTargetFunctionCallProvider(this)
    }
}
