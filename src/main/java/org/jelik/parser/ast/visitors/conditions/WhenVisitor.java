package org.jelik.parser.ast.visitors.conditions;

import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.branching.*;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.token.ArrowToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.WhenKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class WhenVisitor implements TokenVisitor<WhenExpression> {

    private final WhenKeyword whenKeyword;

    public WhenVisitor(WhenKeyword whenKeyword) {
        this.whenKeyword = whenKeyword;
    }

    @Override
    public @NotNull WhenExpression visit(@NotNull ParseContext parseContext) {
        var lexer = parseContext.getLexer();
        var expression = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
        var left = lexer.nextToken();
        var cases = new ArrayList<WhenCaseExpression>();
        Token right = null;
        ElseExpression elseExpression = null;

        while (lexer.hasNextToken()) {
            var next = lexer.nextToken();
            if (next.getTokenType() == ElementType.elseKeyword) {
                var elseBlock = new ConditionBlockVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
                right = parseContext.getLexer().getCurrent();
                elseExpression = new ElseExpressionImpl(((ElseKeyword) next), elseBlock, right);
                break;
            }
            var caseExpr = new ExpressionVisitor(next).visit(parseContext);
            var arrow = lexer.getCurrent();
            TokenUtils.check(arrow, "arrow.expected", parseContext, ArrowToken.class);
            var rightExpr = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
            var casee = new WhenCaseExpressionImpl(caseExpr, rightExpr);
            cases.add(casee);
        }

        assert elseExpression != null;
        return new WhenExpressionImpl(whenKeyword, expression, left, cases, elseExpression, right);
    }
}
