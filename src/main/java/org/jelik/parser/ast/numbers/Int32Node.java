package org.jelik.parser.ast.numbers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Int32Node extends ExpressionWithType {

    private final LiteralToken literalToken;

    private final int value;

    public Int32Node(LiteralToken literalToken, int value) {
        this.literalToken = literalToken;
        this.value = value;
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getEndRow() {
        return literalToken.getEndRow();
    }

    @Override
    public int getEndCol() {
        return literalToken.getEndCol();
    }

    public int getValue() {
        return value;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }
}
