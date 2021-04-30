package org.jelik.utils

import org.apache.commons.lang3.ClassUtils
import org.jelik.compiler.asm.visitor.ToByteCodeResult
import org.jelik.compiler.cl.JelikClassLoader
import org.jelik.compiler.utils.StaticInvocationResult

/**
 * @author Marcin Bukowiecki
 */
class ByteCodeResultWrapper(private val result: List<ToByteCodeResult>) {

    init {
        result.forEach { bc ->
            bc.printByteCode()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> invokeStatic(typeName: String, functionName: String, vararg args: Any): StaticInvocationResult<T> {
        val clazz = result.first { bc -> bc.type.name == typeName }
        val result = JelikClassLoader()
                .defineClass(clazz.type.canonicalName, clazz.bytes)
                .declaredMethods.filter { m ->
                    for ((i, parameterType) in m.parameterTypes.withIndex()) {
                        if (i >= args.size) {
                            return@filter false
                        }
                        if (parameterType.isPrimitive) {
                            if (!ClassUtils.primitiveToWrapper(parameterType).isAssignableFrom(args[i].javaClass)) {
                                return@filter false
                            }
                        } else if (!parameterType.isAssignableFrom(args[i].javaClass)) {
                            return@filter false
                        }
                    }
                    return@filter m.name == functionName
                }
                .first()
                .invoke(null, *args) as T

        return StaticInvocationResult(result)
    }
}
