package org.jelik.parser.ast.arrays;

import lombok.Getter;
import lombok.Setter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.RightBracketToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ArrayOrMapGetExpr extends ExpressionWithType {

    private final LeftBracketToken leftBracketToken;

    @Setter
    private Expression expression;

    private final RightBracketToken rightBracketToken;

    public boolean arrayGet = false;

    public ArrayOrMapGetExpr(@NotNull LeftBracketToken leftBracketToken,
                             @NotNull Expression expression,
                             @NotNull RightBracketToken rightBracketToken) {
        this.leftBracketToken = leftBracketToken;
        this.expression = expression;
        expression.setParent(this);
        this.rightBracketToken = rightBracketToken;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        this.expression = newNode;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
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

    @Override
    public String toString() {
        return leftBracketToken.toString() +
                expression.toString() +
                rightBracketToken.toString() +
                getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
