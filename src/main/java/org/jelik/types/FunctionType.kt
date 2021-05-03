package org.jelik.types

import org.jelik.compiler.common.TypeEnum
import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.data.MethodData

/**
 * @author Marcin Bukowiecki
 */
open class FunctionType : Type {

    constructor(clazz: Class<*>) : super(clazz) {

    }

    constructor(name: String, canonicalName: String) : super(name, canonicalName, TypeEnum.objectT) {

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

    fun getFunctionalInterfaceMethod(compilationContext: CompilationContext): MethodData {
        return findClassData(compilationContext)
            .methodScope
            .first { m -> m.isAbstract && !m.isStatic }
    }
}
