package org.jelik.compiler.exceptions;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.utils.FunctionCompiler;
import org.jelik.parser.exceptions.SyntaxException;
import org.junit.Ignore;
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
                .hasMessage("Unexpected token after function name. Expected parameter list or type variable.");
    }

    @Test
    public void shouldExpectLeftParenthesis() {
        final String expression = "fun a:Int\n { let}";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token after function name. Expected parameter list or type variable.");
    }

    @Test
    public void shouldExpectValName() {
        final String expression = "fun test(Int )\n { }";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Expected parameter type declaration got ')'.");
    }

    @Ignore
    @Test
    public void shouldExpectTypeToken() {
        final String expression = "fun function(: a\n";
        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance().compile(expression))
                .isExactlyInstanceOf(SyntaxException.class)
                .hasMessage("Unexpected token.");
    }
}
