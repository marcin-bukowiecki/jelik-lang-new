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
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElifKeyword;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
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
        var thenKeyword = (ThenKeyword) lexer.nextToken();

        var block = new ConditionBlockVisitor(thenKeyword).visit(parseContext);
        TokenUtils.checkCurrentNotMatching(parseContext, EndKeyword.class, ElseKeyword.class, ElifKeyword.class);
        EndKeyword endKeyword = null;
        if (lexer.getCurrent() instanceof EndKeyword) {
            endKeyword = ((EndKeyword) lexer.getCurrent());
        }

        var ifExpression = new IfExpressionImpl(ifKeyword, condition, thenKeyword, block, endKeyword);
        lastCondition = ifExpression;

        var current = lexer.getCurrent();
        current.accept(this, parseContext);

        return ifExpression;
    }

    @Override
    public void visit(EndKeyword endKeyword, ParseContext parseContext) {

    }

    @Override
    public void visitElseKeyword(ElseKeyword elseKeyword, ParseContext parseContext) {
        var elseBlock = new ConditionBlockVisitor(elseKeyword).visit(parseContext);
        var endKeyword = ((EndKeyword) parseContext.getLexer().getCurrent());
        var elseExpr =  new ElseExpressionImpl(elseKeyword, elseBlock, endKeyword);
        this.lastCondition.setElseExpression(elseExpr);
    }

    @Override
    public void visitElifKeyword(ElifKeyword elifKeyword, ParseContext parseContext) {
        var condition = new IfConditionVisitor(parseContext.getLexer().nextToken())
                .visit(parseContext);

        var thenKeyword = parseContext.getLexer().nextToken();
        if (!(thenKeyword instanceof ThenKeyword)) {
            throw new SyntaxException("Expected then keyword", thenKeyword, parseContext);
        }

        var elseBlock = new ConditionBlockVisitor(thenKeyword).visit(parseContext);
        var current = parseContext.getLexer().getCurrent();
        final EndKeyword endKeyword;
        if (current instanceof EndKeyword) {
            endKeyword = ((EndKeyword) current);
        } else {
            endKeyword = null;
        }
        var elif = new ElifExpression(elifKeyword, condition, ((ThenKeyword) thenKeyword), elseBlock, endKeyword);
        this.lastCondition.setElseExpression(elif);
        this.lastCondition = elif;

        if (endKeyword == null) {
            current.accept(this, parseContext);
        }
    }
}
