package org.jelik.utils

import org.stringtemplate.v4.ST
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * @author Marcin Bukowiecki
 */
object TestGenerator {

    private val template = """
        package org.jelik.generated
        
        import org.jelik.utils.GeneratedTestRunner
        import org.junit.Test

        /**
         * Generated test 
         *        
         * @author Marcin Bukowiecki
         */
        class <testCaseName> : GeneratedTestRunner() {
        
            <tests :{test | <test> }>         
        }
        
    """.trimIndent()

    private val unitTestTemplate = """
                
        @Test
        fun <name>() {
            compileAndRunBox("<path>")
        }       
         
    """.trimIndent()

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @JvmStatic
    fun main(args: Array<String>) {
        val f = File("./src/testData")
        if (f.exists().not()) {
            throw IllegalArgumentException("folder testData does not exists!!!")
        }
        val testCaseNames = f.listFiles()
        assert(testCaseNames != null)
        for (folderName in testCaseNames) {
            val testCaseName = folderName.name.capitalize() + "Test"
            val contents = mutableListOf<String>()

            for (unitTest in folderName.listFiles()) {
                val st = ST(unitTestTemplate)
                st.add("name", unitTest.nameWithoutExtension)
                st.add("path", folderName.name + "/" + unitTest.name)
                contents.add(st.render())
            }

            val st = ST(template)
            st.add("testCaseName", testCaseName)
            st.add("tests", contents)

            val javaCode = st.render()

            Files.writeString(Path.of("./src/test/java/org/jelik/generated/$testCaseName.kt"),
                javaCode,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)
        }
    }
}