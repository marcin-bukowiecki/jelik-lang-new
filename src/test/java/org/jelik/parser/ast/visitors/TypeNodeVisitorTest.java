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
    public void testNullableType_1() {
        var expr = "T?";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T?");
    }

    @Test
    public void testSingleType_1() {
        var expr = "T";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T");
    }

    @Test
    public void testArrayType_1() {
        var expr = "[]T";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("[]T");
    }

    @Test
    public void testSingleType_2() {
        var expr = "T a";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T");
    }

    @Test
    public void testSingleType_3() {
        var expr = "T? a";
        var lexer = Lexer.of(expr);
        var nextToken = lexer.nextToken();
        var visit = new TypeNodeVisitor(nextToken).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("T?");
    }

    @Test
    public void testTupleType_1() {
        var expr = "|Int, Int| -> Int ";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("|Int,Int|->Int");
    }

    @Test
    public void testTupleType_2() {
        var expr = "|| -> Int ";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("||->Int");
    }

    @Test
    public void testTupleType_3() {
        var expr = "|| -> Int";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("||->Int");
    }

    @Test
    public void testPathType_1() {
        var expr = "java.lang.Integer";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("java.lang.Integer");
    }

    @Test
    public void testPathType_2() {
        var expr = "java.util.ArrayList<Int>";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("java.util.ArrayList<Int>");
    }

    @Test
    public void testPathType_3() {
        var expr = "java.util.ArrayList<java.lang.Integer>";
        var lexer = Lexer.of(expr);
        var result = new TypeNodeVisitor(lexer.nextToken()).visit(new ParseContext(lexer));
        Assertions.assertThat(result.toString())
                .isEqualTo("java.util.ArrayList<java.lang.Integer>");
    }
}
