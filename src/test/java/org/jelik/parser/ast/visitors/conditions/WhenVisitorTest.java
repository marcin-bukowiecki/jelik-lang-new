package org.jelik.parser.ast.visitors.conditions;

import org.assertj.core.api.Assertions;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.branching.WhenExpression;
import org.jelik.parser.token.keyword.WhenKeyword;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class WhenVisitorTest {

    @Test
    public void testWhenParse_1() {
        var expr = "when a { 1 -> 1, 2 -> 2, else -> 3 }";
        final Lexer lexer = Lexer.of(expr);
        final ParseContext parseContext = new ParseContext(lexer);
        final WhenExpression whenExpression = new WhenVisitor(((WhenKeyword) lexer.nextToken())).visit(parseContext);
        Assertions.assertThat(whenExpression.toString())
                .isEqualTo("when a { 1 -> 1, 2 -> 2, else { 3 }}");
    }
}