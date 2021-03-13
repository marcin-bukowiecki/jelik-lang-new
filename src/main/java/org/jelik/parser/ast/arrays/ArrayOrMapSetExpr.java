package org.jelik.parser.ast.arrays;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.RightBracketToken;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ArrayOrMapSetExpr extends ExpressionWithType {

    private Expression ref;

    private final LeftBracketToken leftBracketToken;

    private Expression index;

    private final RightBracketToken rightBracketToken;

    private final AssignOperator assignOperator;

    private Expression rightExpression;

    public boolean arraySet = false;

    public ArrayOrMapSetExpr(@NotNull Expression ref,
                             @NotNull LeftBracketToken leftBracketToken,
                             @NotNull Expression index,
                             @NotNull RightBracketToken rightBracketToken,
                             @NotNull AssignOperator assignOperator,
                             @NotNull Expression rightExpression) {
        this.ref = ref;
        this.ref.parent = this;
        this.leftBracketToken = leftBracketToken;
        this.index = index;
        this.index.parent = this;
        this.rightBracketToken = rightBracketToken;
        this.assignOperator = assignOperator;
        this.rightExpression = rightExpression;
        this.rightExpression.parent = this;
        this.nodeContext.setType(JVMVoidType.INSTANCE);
        this.nodeContext.setGenericType(JVMVoidType.INSTANCE);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (ref == oldNode) {
            ref = newNode;
        } else if (rightExpression == oldNode) {
            rightExpression = newNode;
        } else if (index == oldNode) {
            index = newNode;
        }
    }

    @Override
    public int getStartRow() {
        return ref.getStartRow();
    }

    @Override
    public int getStartCol() {
        return ref.getStartCol();
    }

    @Override
    public int getEndRow() {
        return rightExpression.getEndRow();
    }

    @Override
    public int getEndCol() {
        return rightExpression.getEndCol();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return ref.toString() +
                leftBracketToken.toString() +
                index.toString() +
                rightBracketToken.toString() +
                assignOperator.toString() +
                rightExpression.toString();
    }
}
