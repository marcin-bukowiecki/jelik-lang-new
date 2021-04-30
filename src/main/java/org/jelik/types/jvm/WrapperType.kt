package org.jelik.types.jvm

import org.jelik.compiler.asm.MethodVisitorAdapter

/**
 * @author Marcin Bukowiecki
 */
interface WrapperType {

    fun toPrimitiveType(mv: MethodVisitorAdapter)
}
