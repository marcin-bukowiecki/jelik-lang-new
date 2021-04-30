package org.jelik.parser.ast.visitors;

import org.assertj.core.api.Assertions;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.types.WildCardTypeNode;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.LesserOperator;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class TypeParameterListVisitorTest {

    @Test
    public void parse_1() {
        var expr = "<T, R, B>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("T", "R", "B"));
    }

    @Test
    public void parse_2() {
        var expr = "<T>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("T"));
    }

    @Test
    public void parse_3() {
        var expr = "<T, *, B>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("T", "*", "B"));
        Assertions.assertThat(visit.getTypes().get(1))
                .isInstanceOf(WildCardTypeNode.class);
    }

    @Test
    public void parse_4() {
        var expr = "<T : Runnable, R>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("T : Runnable", "R"));
    }

    @Test
    public void parse_5() {
        var expr = "<S, T : Runnable, R>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("S", "T : Runnable", "R"));
    }

    @Test
    public void parse_6() {
        var expr = "<S, T : Runnable>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.getTypes().stream().map(Object::toString).collect(Collectors.toList()))
                .isEqualTo(List.of("S", "T : Runnable"));
    }

    @Test
    public void parse_7() {
        var expr = "<S, T<*>>";
        var lexer = Lexer.of(expr);
        final Token nextToken = lexer.nextToken();
        final TypeVariableListNode visit = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(new ParseContext(lexer));
        Assertions.assertThat(visit.toString())
                .isEqualTo("<S,T<*>>");
    }
}
