package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for parsing operator expressions (handler operator precedence)
 *
 * @author Marcin Bukowiecki
 */
public class OpExpressionVisitor implements TokenVisitor<Expression> {

    private final Expression leftHand;

    private final AbstractOperator operator;

    public OpExpressionVisitor(Expression leftHand, @NotNull AbstractOperator operator) {
        this.leftHand = leftHand;
        this.operator = operator;
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        Token nextToken = lexer.nextToken();

        var rightHand = new RightHandExpressionVisitor(nextToken).visit(parseContext);

        if (!lexer.getCurrent().isOperator()) {
            return operator.toAst(leftHand, rightHand);
        } else {
            AbstractOperator current = (AbstractOperator) lexer.getCurrent();
            if (operator.isHigherPrecedence(current)) {
                var op = this.operator.toAst(leftHand, rightHand);
                return new OpExpressionVisitor(op, current).visit(parseContext);
            } else {
                var newRightHand = new OpExpressionVisitor(rightHand, current).visit(parseContext);
                return operator.toAst(leftHand, newRightHand);
            }
        }
    }
}
