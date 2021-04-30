package org.jelik.parser.ast

import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.ParseContext
import org.jelik.parser.token.DotToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.Token
import org.jelik.parser.token.keyword.ClassKeyword
import org.jelik.parser.token.keyword.FunKeyword
import org.jelik.parser.token.keyword.ImportKeyword
import org.jelik.parser.token.keyword.PackageKeyword

/**
 * @author Marcin Bukowiecki
 */
class PackageVisitor(private val packageKeyword: PackageKeyword) :
    TokenVisitor<PackageDeclaration> {

    private val tokens = mutableListOf<Token>()

    override fun visit(parseContext: ParseContext): PackageDeclaration {
        val lexer = parseContext.lexer

        while (lexer.hasNextToken()) {
            if (lexer.peekNext() is ImportKeyword ||
                    lexer.peekNext() is FunKeyword ||
                    lexer.peekNext() is ClassKeyword) {
                break
            }

            val next = lexer.nextToken()
            if (next is LiteralToken) {
                tokens.add(next)
                continue
            }
            if (next is DotToken) {
                tokens.add(next)
                continue
            }
            throw SyntaxException("Unexpected token", next, parseContext.currentFilePath)
        }

        return PackageDeclaration(packageKeyword, tokens)
    }
}
