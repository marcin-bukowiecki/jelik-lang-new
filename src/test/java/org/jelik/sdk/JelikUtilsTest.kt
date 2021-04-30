package org.jelik.sdk

import org.jelik.compiler.JelikCompiler
import org.jelik.utils.ByteCodeResultWrapper
import org.junit.Test
import java.io.File

/**
 * @author Marcin Bukowiecki
 */
class JelikUtilsTest {

    @Test
    fun calculateArrayIndexForCollection_1() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", listOf(1,2,3,4), 2)
                .isEqualTo(2)
    }

    @Test
    fun calculateArrayIndexForCollection_2() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", listOf(1,2,3,4), -2)
                .isEqualTo(2)
    }

    @Test
    fun calculateArrayIndexForCollection_3() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", listOf(1,2,3,4), 0)
                .isEqualTo(0)
    }

    @Test
    fun calculateArrayIndexForCollection_4() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", listOf(1,2,3,4), -1)
                .isEqualTo(3)
    }

    @Test
    fun calculateArrayIndexForIntArray_1() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", intArrayOf(1,2,3,4), 2)
                .isEqualTo(2)
    }

    @Test
    fun calculateArrayIndexForIntArray_2() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", intArrayOf(1,2,3,4), -2)
                .isEqualTo(2)
    }

    @Test
    fun calculateArrayIndexForIntArray_3() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", intArrayOf(1,2,3,4), 0)
                .isEqualTo(0)
    }

    @Test
    fun calculateArrayIndexForIntArray_4() {
        ByteCodeResultWrapper(JelikCompiler.INSTANCE
                .compile(File("./sdk/jelik/lang/JelikUtils.jlk")))
                .invokeStatic<Int>("JelikUtils", "calculateArrayIndex", intArrayOf(1,2,3,4), -1)
                .isEqualTo(3)
    }
}
