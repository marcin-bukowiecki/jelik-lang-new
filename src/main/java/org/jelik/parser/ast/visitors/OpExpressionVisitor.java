package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jelik.parser.token.operators.LesserOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for parsing operator expressions (handler operator precedence)
 *
 * @author Marcin Bukowiecki
 */
public class OpExpressionVisitor implements ParseVisitor<Expression> {

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

        if (operator instanceof LesserOperator && parseContext.getLexer().getCurrent() instanceof GreaterOperator) {
            parseContext.getLexer().recover(operator);
            nextToken = parseContext.getLexer().nextToken();
            final TypeNode typeNode = new TypeNodeVisitor(nextToken).visit(parseContext);
            leftHand.setGenericTypeNode(typeNode);
            final ExpressionVisitor expressionVisitor = new ExpressionVisitor(leftHand, leftHand);
            parseContext.getLexer().nextToken();
            parseContext.getLexer().nextToken().visit(expressionVisitor, parseContext);
            return expressionVisitor.currentExpression;
        }

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
