package org.jelik.parser.ast.arrays;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
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
public class ArrayOrMapSetExpr extends TypedExpression {

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
        this.ref.setParent(this);
        this.leftBracketToken = leftBracketToken;
        this.index = index;
        this.index.setParent(this);
        this.rightBracketToken = rightBracketToken;
        this.assignOperator = assignOperator;
        this.rightExpression = rightExpression;
        this.rightExpression.setParent(this);
        this.nodeContext.setType(JVMVoidType.INSTANCE);
        this.nodeContext.setGenericType(JVMVoidType.INSTANCE);
    }

    public Expression getRef() {
        return ref;
    }

    public LeftBracketToken getLeftBracketToken() {
        return leftBracketToken;
    }

    public Expression getIndex() {
        return index;
    }

    public RightBracketToken getRightBracketToken() {
        return rightBracketToken;
    }

    public AssignOperator getAssignOperator() {
        return assignOperator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    public boolean isArraySet() {
        return arraySet;
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

    public Type getValueType() {
        return arraySet ? ref.getGenericReturnType().getInnerType(0) :
                ref.getGenericReturnType().getTypeVariables().get(1);
    }

    public Type getKeyType() {
        if (arraySet) {
            throw new UnsupportedOperationException();
        } else {
            return ref.getGenericReturnType().getTypeVariables().get(0);
        }
    }

    @Override
    public int getStartOffset() {
        return ref.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return rightBracketToken.getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitArrayOrMapSetExpr(this, compilationContext);
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
