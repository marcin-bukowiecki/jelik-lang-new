package org.jelik.compiler

import org.jelik.compiler.data.JelikBuiltinFunction
import org.jelik.compiler.data.MethodData
import org.jelik.instrincs.JelikAny
import org.jelik.types.JVMArrayType
import org.jelik.types.JVMIntType
import org.objectweb.asm.Opcodes

/**
 * @author Marcin Bukowiecki
 */
object JelikBuiltinFunctionsRegister {

    val registers: List<MethodData> = listOf(
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMIntType.INSTANCE)),
                    listOf(JVMArrayType(JVMIntType.INSTANCE)),
                            JVMIntType.INSTANCE,
                            JVMIntType.INSTANCE,
                    JelikAny.classData) { mv ->
                mv.visitArrayLength()
            })

    fun findByName(name: String): List<MethodData> {
        return registers.filter { m -> m.name == name }
    }
}
