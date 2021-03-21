package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.branching.ElseExpression;
import org.jelik.parser.ast.branching.IfConditionExpression;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IfVisitor implements ParseVisitor<IfExpression> {

    private final IfKeyword ifKeyword;

    private IfExpression ifExpression;

    private IfConditionExpression condition;

    private ThenKeyword thenKeyword;

    private BasicBlock block;

    private Expression furtherExpression;

    public IfVisitor(IfKeyword ifKeyword) {
        this.ifKeyword = ifKeyword;
    }

    @Override
    public @NotNull IfExpression visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();

        Token nextToken = lexer.nextToken();
        this.condition = new IfConditionVisitor(nextToken).visit(parseContext);
        Token current = lexer.nextToken();
        if (current.getTokenType() == ElementType.thenKeyword) {
            this.thenKeyword = ((ThenKeyword) current);
        } else {
            throw new SyntaxException("Expected then keyword after if condition", current, parseContext.getCurrentFilePath());
        }

        this.block = new ConditionBlockVisitor().visit(parseContext);

        current = lexer.getCurrent();
        current.visit(this, parseContext);

        ifExpression.setFurtherExpression(furtherExpression);

        return ifExpression;
    }

    @Override
    public void visit(EndKeyword endKeyword, ParseContext parseContext) {
        this.ifExpression = new IfExpression(ifKeyword, condition, thenKeyword, block, endKeyword);
    }

    @Override
    public void visitElseKeyword(ElseKeyword elseKeyword, ParseContext parseContext) {
        this.ifExpression = new IfExpression(ifKeyword, condition, thenKeyword, block, null);
        var elseBlock = new ConditionBlockVisitor().visit(parseContext);
        var endKeyword = ((EndKeyword) parseContext.getLexer().getCurrent());
        if (furtherExpression == null) {
            furtherExpression = new ElseExpression(elseKeyword, elseBlock, endKeyword);
        } else {
            furtherExpression.setFurtherExpression(new ElseExpression(elseKeyword, elseBlock, endKeyword));
        }
    }
}
