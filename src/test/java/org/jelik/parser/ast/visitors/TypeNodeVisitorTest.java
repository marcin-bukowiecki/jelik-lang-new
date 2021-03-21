package org.jelik.parser.ast.visitors;

import org.assertj.core.api.Assertions;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class TypeNodeVisitorTest {

    @Test
    public void nullableType_1() {
        var expr = "T?";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T?");
    }

    @Test
    public void singleType_1() {
        var expr = "T";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T");
    }

    @Test
    public void arrayType_1() {
        var expr = "[]T";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("[]T");
    }
}
