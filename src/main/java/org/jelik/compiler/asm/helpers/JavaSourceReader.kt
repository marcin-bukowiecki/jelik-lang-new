package org.jelik.compiler.asm.helpers

import org.jelik.compiler.data.JavaClassData
import org.objectweb.asm.ClassReader

/**
 * @author Marcin Bukowiecki
 */
class JavaSourceReader(bytes: ByteArray) : ClassReader(bytes) {

    fun get(): JavaClassData {
        return null!!
    }
}
