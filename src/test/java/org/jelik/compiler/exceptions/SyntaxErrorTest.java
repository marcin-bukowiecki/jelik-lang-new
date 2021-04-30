package org.jelik.compiler.exceptions;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class SyntaxErrorTest {

    @Test
    public void shouldThrowLeftParenthesisExpected() {
        var expression = "fun adder Int";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token");
    }

    @Test
    public void shouldExpectLeftParenthesis() {
        final String expression = "fun a:Int\n { let}";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token");
    }

    @Test
    public void shouldExpectValName() {
        final String expression = "fun test(Int )\n { }";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Expected parameter type declaration got )");
    }

    @Test
    public void shouldExpectTypeToken() {
        final String expression = "fun function(: a\n";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Expected parameter name.");
    }

    @Test
    public void unexpectedToken_1() {
        final String expression = "fu\n";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token");
    }

    @Test
    public void unexpectedToken_2() {
        final String expression = "fu";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token");
    }

    @Test
    public void unexpectedToken_3() {
        final String expression = "fun class";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Expected function name");
    }

    @Test
    public void unexpectedToken_4() {
        final String expression = "fun";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Expected function name");
    }

    @Test
    public void unexpectedToken_5() {
        final String expression = "fun test {";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token after function name. Expected parameter list or type variable");
    }


}
