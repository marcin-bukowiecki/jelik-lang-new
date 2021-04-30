package org.jelik.parser.ast.visitors

import org.assertj.core.api.Assertions
import org.jelik.parser.Lexer
import org.jelik.parser.ParseContext
import org.jelik.parser.token.keyword.ClassKeyword
import org.junit.Test
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class ClassDeclarationVisitorTest {

    @Test
    fun parseClass_1() {
        val expr = "class Test"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val clazz = ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        Assertions.assertThat(clazz.toString())
                .isEqualTo("class Test{\n\n}")
    }

    @Test
    fun parseClass_2() {
        val expr = "class Test { val a Int = 10 }"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val clazz = ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        Assertions.assertThat(clazz.fieldDeclarations.map { fd -> fd.toString() })
                .isEqualTo(Collections.singletonList("val a Int = 10"))
    }

    @Test
    fun parseClass_3() {
        val expr = "class Test { val a Int = 10 val }"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        Assertions.assertThatThrownBy {
            ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        }.hasMessage("Expected new line before: 'val'")
    }

    @Test
    fun parseClass_4() {
        val expr = "class Test { val a Int = 10 var }"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        Assertions.assertThatThrownBy {
            ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        }.hasMessage("Expected new line before: 'var'")
    }

    @Test
    fun parseClass_5() {
        val expr = "class Test { val a Int = 10 \n var b Int = 11 }"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val clazz = ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        Assertions.assertThat(clazz.fieldDeclarations.map { fd -> fd.toString() })
                .isEqualTo(listOf("val a Int = 10", "var b Int = 11"))
    }

    @Test
    fun parseClass_6() {
        val expr = "class Test{}"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val clazz = ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        Assertions.assertThat(clazz.toString())
                .isEqualTo("class Test{\n\n}")
    }

    @Test
    fun parseClass_7() {
        val expr = "class Tuple1<T1> {\n" +
                "constructor(_1 T1) {}\n" +
                "}"
        val lexer = Lexer.of(expr)
        val parseContext = ParseContext(lexer)
        val clazz = ClassDeclarationVisitor(lexer.nextToken() as ClassKeyword).visit(parseContext)
        Assertions.assertThat(clazz.toString())
                .isEqualTo("class Tuple1<T1>{\n" +
                        "constructor(_1 T1){}\n" +
                        "}")
    }
}

