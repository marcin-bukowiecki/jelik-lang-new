package org.jelik.parser.ast;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReturnExpr extends ExpressionWithType implements ConsumingExpression, StackConsumer {

    private final ReturnKeyword returnKeyword;

    public ReturnExpr(ReturnKeyword returnKeyword, Expression expression) {
        this.returnKeyword = returnKeyword;
        this.setFurtherExpression(expression);
    }

    @Override
    public int getStartRow() {
        return returnKeyword.getRow();
    }

    @Override
    public int getStartCol() {
        return returnKeyword.getCol();
    }

    @Override
    public int getEndCol() {
        return furtherExpression.getEndCol();
    }

    @Override
    public int getEndRow() {
        return furtherExpression.getEndRow();
    }

    public ReturnKeyword getReturnKeyword() {
        return returnKeyword;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (oldNode == furtherExpression) {
            furtherExpression = newNode;
            newNode.parent = this;
        } else {
            throw new IllegalArgumentException("Could not find node for replace: " + oldNode);
        }
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitReturnExpr(this, compilationContext);
    }

    @Override
    public String toString() {
        return returnKeyword + " " + (furtherExpression == null ? "" : furtherExpression.toString());
    }
}
