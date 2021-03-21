package org.jelik.compiler.helper

import org.jelik.compiler.asm.helpers.JavaSourceReader
import org.jelik.compiler.data.JavaClassData
import java.nio.file.Files

/**
 * @author Marcin Bukowiecki
 */
class SourceFiles(val jelikSourceFiles: List<JelikSourceFile>,
                  val javaSourceFiles: List<JavaSourceFile>) {

    /**
     * Map found Java source files to [JavaClassData]
     */
    fun mapToJavaClassData(): List<JavaClassData> {
        return if (javaSourceFiles.isEmpty()) {
            emptyList()
        } else {
            return javaSourceFiles
                    .map { javaSourceFile -> Files.readAllBytes(javaSourceFile.file.toPath()) }
                    .map { bytes -> JavaSourceReader(bytes).get() }
        }
    }

    companion object {

        fun default(): SourceFiles {
            return SourceFiles(emptyList(), emptyList())
        }
    }
}
