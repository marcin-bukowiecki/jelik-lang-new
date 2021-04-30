package org.jelik.utils

import org.jelik.compiler.JelikCompiler
import org.jelik.compiler.cl.JelikClassLoader
import org.jelik.compiler.utils.FunctionCompiler
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver
import java.io.File

/**
 * @author Marcin Bukowiecki
 */
object DependencyLoader {

    @JvmStatic
    fun loadDefaultDependency(path: String, classLoader: JelikClassLoader) {
        JelikCompiler.INSTANCE.compile(File(path))
                .forEach { bc ->
                    classLoader.defineClass(bc.type.canonicalName, bc.bytes)
                    DefaultImportedTypeResolver.addDefaultImport(
                            bc.type.canonicalName,
                            bc.type)
                }
    }

    @JvmStatic
    fun loadDefaultDependency(path: String) {
        loadDefaultDependency(path, FunctionCompiler.getInstance().jelikClassLoader)
    }
}
