package org.jelik.parser.ast.operators;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public abstract class AbstractOpExpr extends ExpressionWithType {

    protected Expression left;

    protected final AbstractOperator op;

    protected Expression right;

    public AbstractOpExpr(Expression left, AbstractOperator op, Expression right) {
        this.left = left;
        left.setParent(this);
        this.op = op;
        this.right = right;
        right.setParent(this);
    }

    public AbstractOpExpr(AbstractOperator op, Expression right) {
        this.op = op;
        this.right = right;
        right.setParent(this);
    }

    @Override
    public @NotNull List<ASTNode> getChildren() {
        List<ASTNode> result = new ArrayList<>();
        if (left != null) result.add(left);
        if (right != null) result.add(right);
        return result;
    }

    public AbstractOperator getOp() {
        return op;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (left == oldNode) {
            left = newNode;
            newNode.setParent(this);
        } else if (right == oldNode) {
            right = newNode;
            newNode.setParent(this);
        } else {
            throw new IllegalArgumentException("Could not find given old node to replace: " + oldNode);
        }
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int getStartRow() {
        return left.getStartRow();
    }

    @Override
    public int getEndRow() {
        return right.getEndRow();
    }

    @Override
    public int getStartCol() {
        return left.getStartCol();
    }

    @Override
    public int getEndCol() {
        return right.getEndCol();
    }

    @Override
    public String toString() {
        return (left == null ? "" : left) + " " + op + " " + right;
    }
}
