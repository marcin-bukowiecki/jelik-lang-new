package org.jelik.compiler.helper

import org.jelik.CompilationContext
import java.io.File
import kotlin.system.exitProcess

/**
 * @author Marcin Bukowiecki
 */
object SourceFilesProvider {

    fun findSourceFiles(compilationContext: CompilationContext): SourceFiles {
        val compileDirectory = compilationContext.compileDirectory
        val file = File(compileDirectory)
        if (!file.exists()) {
            println("${compilationContext.compileDirectory} directory does not exist")
            exitProcess(1)
        }
        if (!file.isDirectory) {
            println("${compilationContext.compileDirectory} is not a directory")
            exitProcess(1)
        }

        val javaFiles = mutableListOf<JavaSourceFile>()
        getFiles(javaFiles, file, "java") { f -> JavaSourceFile(f) }
        val jelikFiles = mutableListOf<JelikSourceFile>()
        getFiles(jelikFiles, file, "jlk") { f -> JelikSourceFile(f) }

        return SourceFiles(javaSourceFiles = javaFiles, jelikSourceFiles = jelikFiles)
    }

    private fun <T : SourceFile> getFiles(acc: MutableList<T>, dir: File, extension: String, factory: (File) -> T) {
        for (f in dir.listFiles() ?: emptyArray()) {
            if (f.isDirectory) {
                getFiles(acc, f, extension, factory)
            } else {
                if (f.extension == extension) {
                    acc.add(factory(f))
                }
            }
        }
    }
}
