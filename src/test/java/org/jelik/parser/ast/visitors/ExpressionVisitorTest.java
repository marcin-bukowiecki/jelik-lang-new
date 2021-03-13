package org.jelik.parser.ast.visitors;

import org.assertj.core.api.Assertions;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.Token;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class ExpressionVisitorTest {

    @Test
    public void testAPlusB() {
        ParseContext parseContext = new ParseContext("ret a + b");
        Token nextToken = parseContext.getLexer() .nextToken();
        Assertions.assertThat(nextToken)
                .hasFieldOrPropertyWithValue("elementType", ElementType.returnKeyword);
        Expression expr = new ReturnExpressionVisitor(((ReturnKeyword) nextToken)).visit(parseContext);
        System.out.println(expr);
        Assertions.assertThat(expr.toString()).isEqualTo("ret a + b");
        Assertions.assertThat(expr)
                .isInstanceOf(ReturnExpr.class)
                .matches(e -> e.getFurtherExpressionOpt().isPresent() &&
                        e.getFurtherExpressionOpt().get() instanceof AddExpr &&
                        ((AddExpr) e.getFurtherExpressionOpt().get()).getLeft() instanceof LiteralExpr &&
                        ((AddExpr) e.getFurtherExpressionOpt().get()).getRight() instanceof LiteralExpr);
    }

    @Test
    public void testAbcd() {
        ParseContext parseContext = new ParseContext("ret a.b.c.d");
        Token nextToken = parseContext.getLexer().nextToken();
        Assertions.assertThat(nextToken)
                .hasFieldOrPropertyWithValue("elementType", ElementType.returnKeyword);
        Expression expr = new ReturnExpressionVisitor(((ReturnKeyword) nextToken)).visit(parseContext);
        System.out.println(expr);
        Assertions.assertThat(expr.toString()).isEqualTo("ret a.b.c.d");
        Assertions.assertThat(expr)
                .isInstanceOf(ReturnExpr.class)
                .matches(e -> e.getFurtherExpressionOpt().isPresent() &&
                        e.getFurtherExpressionOpt().get() instanceof LiteralExpr &&
                        e.getFurtherExpressionOpt().get().getFurtherExpressionOpt().isPresent() &&
                        e.getFurtherExpressionOpt().get().getFurtherExpressionOpt().get() instanceof DotCallExpr);
    }

    @Test
    public void testAbCallCd() {
        ParseContext parseContext = new ParseContext("ret a.b.c().d");
        Token nextToken = parseContext.getLexer().nextToken();
        Assertions.assertThat(nextToken)
                .hasFieldOrPropertyWithValue("elementType", ElementType.returnKeyword);
        Expression expr = new ReturnExpressionVisitor(((ReturnKeyword) nextToken)).visit(parseContext);
        System.out.println(expr);
        Assertions.assertThat(expr.toString()).isEqualTo("ret a.b.c().d");
        Assertions.assertThat(expr)
                .isInstanceOf(ReturnExpr.class)
                .matches(e -> e.getFurtherExpressionOpt().isPresent() && e.getFurtherExpressionOpt().get() instanceof LiteralExpr);
    }

    @Test
    public void testAPlusBMulCPlusD() {
        ParseContext parseContext = new ParseContext("ret a + b * c + d");
        Token nextToken = parseContext.getLexer().nextToken();
        Assertions.assertThat(nextToken)
                .hasFieldOrPropertyWithValue("elementType", ElementType.returnKeyword);
        Expression expr = new ReturnExpressionVisitor(((ReturnKeyword) nextToken)).visit(parseContext);
        System.out.println(expr);
        Assertions.assertThat(expr.toString()).isEqualTo("ret a + b * c + d");
        Assertions.assertThat(expr)
                .isInstanceOf(ReturnExpr.class)
                .matches(e -> e.getFurtherExpression() instanceof AddExpr &&
                        ((AddExpr) e.getFurtherExpression()).getRight() instanceof AddExpr);
    }
}
