package org.jelik.utils

import org.assertj.core.api.Assertions
import org.jelik.compiler.JelikCompiler
import org.jelik.compiler.cl.JelikClassLoader
import java.io.File

/**
 * @author Marcin Bukowiecki
 */
open class GeneratedTestRunner {

    fun compileAndRunBox(path: String) {
        val jelikClassLoader = JelikClassLoader()
        JelikCompiler.INSTANCE.loadSdk(jelikClassLoader)
        val toByteCodeResult = JelikCompiler.INSTANCE.compile(File("./src/testData/$path"))[0]
        toByteCodeResult.printByteCode()
        val clazz = jelikClassLoader.defineClass(toByteCodeResult.type.canonicalName, toByteCodeResult.bytes)
        val declaredMethod = clazz.getDeclaredMethod("box")
        Assertions.assertThat(declaredMethod.invoke(null) as Boolean)
            .isTrue
    }
}