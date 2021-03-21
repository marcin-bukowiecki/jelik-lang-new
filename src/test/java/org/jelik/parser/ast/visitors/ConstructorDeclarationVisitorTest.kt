package org.jelik.parser.ast.visitors

import org.assertj.core.api.Assertions
import org.jelik.parser.Lexer
import org.jelik.parser.ParseContext
import org.jelik.parser.token.keyword.ConstructorKeyword
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ConstructorDeclarationVisitorTest {

    @Test
    fun parse_1() {
        val expr = "constructor() {}"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val result = ConstructorDeclarationVisitor(lexer.nextToken() as ConstructorKeyword).visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("constructor(){}")
    }

    @Test
    fun parse_2() {
        val expr = "constructor(a Int) {}"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val result = ConstructorDeclarationVisitor(lexer.nextToken() as ConstructorKeyword).visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("constructor(a Int){}")
    }

    @Test
    fun parse_3() {
        val expr = "constructor(a Int, b Int) {}"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val result = ConstructorDeclarationVisitor(lexer.nextToken() as ConstructorKeyword).visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("constructor(a Int, b Int){}")
    }
}
