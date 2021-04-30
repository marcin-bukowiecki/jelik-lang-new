package org.jelik.parser.ast

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.Lexer
import org.jelik.parser.ParseContext
import org.jelik.parser.token.keyword.FunKeyword
import java.io.File
import java.nio.file.Files

/**
 * @author Marcin Bukowiecki
 */
object JelikASTFactory {

    private const val tempFileName = "jlk_temp.jlk"

    fun createExpressionFromText(ctx: CompilationContext, text: String?): ASTNode? {
        if (text == null) return null

        val createTempDir = File(tempFileName)
        if (!createTempDir.exists()) {
            createTempDir.createNewFile();
        }
        Files.writeString(createTempDir.toPath(), text)
        val nextToken =  ParseContext(Lexer(tempFileName)).lexer.nextToken()

        if (nextToken !is FunKeyword) {
            val tempFun = "package repl\nfun temp_1() {\nret $text\n}\n"
            Files.writeString(createTempDir.toPath(), tempFun)
            return ModuleParser(tempFileName).visit(ParseContext(Lexer(tempFileName)))
        }

        return null
    }
}
