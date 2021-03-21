package org.jelik.parser.ast.visitors;

import org.assertj.core.api.Assertions;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.token.keyword.VarKeyword;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class VariableVisitorTest {

    @Test
    public void parse_1() {
        var expr = "var a Int = expr";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var result = new VariableVisitor((VarKeyword) nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("var a Int = expr");
    }

    @Test
    public void parse_2() {
        var expr = "var a = expr";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var result = new VariableVisitor((VarKeyword) nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("var a = expr");
    }

    @Test
    public void parse_3() {
        var expr = "var a = expr + expr";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var result = new VariableVisitor((VarKeyword) nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("var a = expr + expr");
    }
}
