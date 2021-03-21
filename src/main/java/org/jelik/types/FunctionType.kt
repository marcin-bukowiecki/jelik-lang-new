package org.jelik.types

import org.jelik.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.common.TypeEnum
import org.jelik.compiler.data.LambdaMethodData
import org.jelik.compiler.data.MethodData
import org.jelik.compiler.locals.LocalVariable
import org.jelik.parser.ast.functions.FunctionReferenceNode
import org.jelik.parser.ast.types.InferredTypeRef
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
open class FunctionType: Type {

    constructor(clazz: Class<*>): super(clazz) {

    }

    constructor(name: String, canonicalName: String): super(name, canonicalName, TypeEnum.objectT) {

    }

    companion object {

        fun getFunctionType(args: Int, returnType: Type): FunctionType {
            when (args) {
                0 -> {
                    return if (returnType.isVoid) {
                        RunnableType()
                    } else {
                        SupplierType()
                    }
                }
                1 -> {
                    return if (returnType.isVoid) {
                        ConsumerType()
                    } else {
                        Function1Type()
                    }
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun getFunctionalInterfaceMethod(lv: LocalVariable,
                                     compilationContext: CompilationContext): Optional<MethodData> {
        return Optional.ofNullable(
                LambdaMethodData(
                        (lv.typeRef as InferredTypeRef).ref as FunctionReferenceNode,
                        { mv: MethodVisitorAdapter ->
                            mv.aload(lv.getIndex())
                        },
                        findClassData (compilationContext).methodScope.first { m -> !m.isStatic && m.isAbstract }))
    }
}
