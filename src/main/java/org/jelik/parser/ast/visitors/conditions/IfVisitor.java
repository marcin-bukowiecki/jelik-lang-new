package org.jelik.parser.ast.visitors.conditions;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.branching.ConditionExpression;
import org.jelik.parser.ast.branching.ElifExpression;
import org.jelik.parser.ast.branching.ElseExpressionImpl;
import org.jelik.parser.ast.branching.IfExpressionImpl;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElifKeyword;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IfVisitor implements TokenVisitor<IfExpressionImpl> {

    private final IfKeyword ifKeyword;

    private ConditionExpression<?> lastCondition;

    public IfVisitor(IfKeyword ifKeyword) {
        this.ifKeyword = ifKeyword;
    }

    @Override
    public @NotNull IfExpressionImpl visit(@NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        final Token nextToken = lexer.nextToken();

        var condition = new IfConditionVisitor(nextToken).visit(parseContext);
        var leftCurl = (LeftCurlToken) lexer.getCurrent();

        var block = new ConditionBlockVisitor(leftCurl).visit(parseContext);
        TokenUtils.checkCurrent("token.unexpected", parseContext, RightCurlToken.class);
        RightCurlToken rightCurl = null;
        if (lexer.getCurrent() instanceof RightCurlToken) {
            rightCurl = ((RightCurlToken) lexer.getCurrent());
        }

        var ifExpression = new IfExpressionImpl(ifKeyword, condition, leftCurl, block, rightCurl);
        lastCondition = ifExpression;

        var peeked = lexer.peekNext();
        if (peeked.getTokenType() == ElementType.elifKeyword || peeked.getTokenType() == ElementType.elseKeyword) {
            lexer.nextToken().accept(this, parseContext);
        }

        return ifExpression;
    }

    @Override
    public void visitElseKeyword(ElseKeyword elseKeyword, ParseContext parseContext) {
        var elseBlock = new ConditionBlockVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
        var rightCurlToken = ((RightCurlToken) parseContext.getLexer().getCurrent());
        var elseExpr =  new ElseExpressionImpl(elseKeyword, elseBlock, rightCurlToken);
        this.lastCondition.setElseExpression(elseExpr);
    }

    @Override
    public void visitElifKeyword(ElifKeyword elifKeyword, ParseContext parseContext) {
        var condition = new IfConditionVisitor(parseContext.getLexer().nextToken())
                .visit(parseContext);

        var leftCurl = parseContext.getLexer().getCurrent();
        if (!(leftCurl instanceof LeftCurlToken)) {
            throw new SyntaxException("Expected '{'", leftCurl, parseContext);
        }

        var elseBlock = new ConditionBlockVisitor(leftCurl).visit(parseContext);
        var current = parseContext.getLexer().getCurrent();
        final RightCurlToken rightCurl;
        if (current instanceof RightCurlToken) {
            rightCurl = ((RightCurlToken) current);
        } else {
            rightCurl = null;
        }
        var elif = new ElifExpression(elifKeyword, condition, leftCurl, elseBlock, rightCurl);
        this.lastCondition.setElseExpression(elif);
        this.lastCondition = elif;

        if (parseContext.getLexer().peekNext().getTokenType() == ElementType.elseKeyword) {
            parseContext.getLexer().nextToken().accept(this, parseContext);
        } else if (parseContext.getLexer().peekNext().getTokenType() == ElementType.elifKeyword) {
            parseContext.getLexer().nextToken().accept(this, parseContext);
        }
    }
}
