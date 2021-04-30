package org.jelik.parser.ast.arrays;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.slice.ArrayGetSliceProvider;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.operators.SliceExpr;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.RightBracketToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ArrayOrMapGetExpr extends ExpressionWithType {

    private Expression leftExpr;

    private final LeftBracketToken leftBracketToken;

    private Expression expression;

    private final RightBracketToken rightBracketToken;

    public boolean arrayGet = false;

    public ArrayOrMapGetExpr(@NotNull Expression leftExpr,
                             @NotNull LeftBracketToken leftBracketToken,
                             @NotNull Expression expression,
                             @NotNull RightBracketToken rightBracketToken) {

        this.leftExpr = leftExpr;
        leftExpr.setParent(this);
        this.leftBracketToken = leftBracketToken;
        this.expression = expression;
        expression.setParent(this);
        this.rightBracketToken = rightBracketToken;
    }

    public boolean isArraySlice() {
        return expression instanceof SliceExpr;
    }

    @Override
    public @NotNull ASTNode getParent() {
        return super.getParent();
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (oldNode == leftExpr) {
            leftExpr = newNode;
        } else if (oldNode == expression) {
            expression = newNode;
        } else {
            super.replaceWith(oldNode, newNode);
        }
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitArrayOrMapGetExpr(this, compilationContext);
    }

    @Override
    public int getStartCol() {
        return leftBracketToken.getCol();
    }

    @Override
    public int getEndCol() {
        return rightBracketToken.getCol();
    }

    @Override
    public int getStartRow() {
        return leftBracketToken.getRow();
    }

    @Override
    public int getEndRow() {
        return rightBracketToken.getRow();
    }

    public ArrayGetElementProvider getNextElementProvider() {
        if (isArraySlice()) {
            return new ArrayGetSliceProvider(this);
        } else if (arrayGet) {
            return new ArrayGetElementProvider(this);
        } else {
            return new MapGetElementProvider(this);
        }
    }

    public LeftBracketToken getLeftBracketToken() {
        return leftBracketToken;
    }

    public Expression getLeftExpr() {
        return leftExpr;
    }

    public void setLeftExpr(Expression leftExpr) {
        this.leftExpr = leftExpr;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public RightBracketToken getRightBracketToken() {
        return rightBracketToken;
    }

    public boolean isArrayGet() {
        return arrayGet;
    }

    public void setArrayGet(boolean arrayGet) {
        this.arrayGet = arrayGet;
    }

    @Override
    public String toString() {
        return leftExpr.toString() +
                leftBracketToken.toString() +
                expression.toString() +
                rightBracketToken.toString();
    }
}
