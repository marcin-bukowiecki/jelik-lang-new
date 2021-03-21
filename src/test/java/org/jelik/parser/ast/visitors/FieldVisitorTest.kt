package org.jelik.parser.ast.visitors

import org.assertj.core.api.Assertions
import org.jelik.parser.Lexer
import org.jelik.parser.ParseContext
import org.jelik.parser.token.keyword.Modifier
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class FieldVisitorTest {

    @Test
    fun parse_1() {
        val expr = "var a Int = expr"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        val result = FieldVisitor().visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("var a Int = expr")
    }

    @Test
    fun parse_2() {
        val expr = "var a = expr"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        val result = FieldVisitor().visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("var a UNDEFINED = expr")
    }

    @Test
    fun parse_3() {
        val expr = "val a Int = expr"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        val result = FieldVisitor().visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("val a Int = expr")
    }

    @Test
    fun parse_4() {
        val expr = "val a = expr"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        val result = FieldVisitor().visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("val a UNDEFINED = expr")
    }

    @Test
    fun parse_5() {
        val expr = "var a Int"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        val result = FieldVisitor().visit(parseContext)
        Assertions.assertThat(result.toString())
                .isEqualTo("var a Int")
    }

    @Test
    fun parse_6() {
        val expr = "val a = "
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        Assertions.assertThatThrownBy {
            FieldVisitor().visit(parseContext)
        }.hasMessage("Expected value definition")
    }

    @Test
    fun parse_7() {
        val expr = "val a"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        Assertions.assertThatThrownBy {
            FieldVisitor().visit(parseContext)
        }.hasMessage("Expected value definition")
    }

    @Test
    fun parse_8() {
        val expr = "var a = "
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        Assertions.assertThatThrownBy {
            FieldVisitor().visit(parseContext)
        }.hasMessage("Expected variable definition")
    }

    @Test
    fun parse_9() {
        val expr = "val a Int"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        Assertions.assertThatThrownBy {
            FieldVisitor().visit(parseContext)
        }.hasMessage("Expected value definition")
    }

    @Test
    fun parse_10() {
        val expr = "val ="
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        parseContext.modifiersStack.add(lexer.nextToken() as Modifier)
        Assertions.assertThatThrownBy {
            FieldVisitor().visit(parseContext)
        }.hasMessage("Expected field name")
    }
}
