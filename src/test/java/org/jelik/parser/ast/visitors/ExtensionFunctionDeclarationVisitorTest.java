package org.jelik.parser.ast.visitors;

import org.assertj.core.api.Assertions;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.visitors.functions.ExtensionFunctionDeclarationVisitor;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class ExtensionFunctionDeclarationVisitorTest {

    @Test
    public void test_parse_1() {
        var expr = "ext fun T let<T>() {}";
        var lexer = Lexer.of(expr);
        var fd = new ExtensionFunctionDeclarationVisitor(lexer.nextToken(), lexer.nextToken())
                .visit(new ParseContext(lexer));

        Assertions.assertThat(fd.getText())
                .isEqualTo("ext fun T let<T>() {}");
    }

    @Test
    public void test_parse_2() {
        var expr = "ext fun String let() {}";
        var lexer = Lexer.of(expr);
        var fd = new ExtensionFunctionDeclarationVisitor(lexer.nextToken(), lexer.nextToken())
                .visit(new ParseContext(lexer));

        Assertions.assertThat(fd.getText())
                .isEqualTo("ext fun String let() {}");
    }
}
