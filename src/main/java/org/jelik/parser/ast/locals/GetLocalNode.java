package org.jelik.parser.ast.locals;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GetLocalNode extends ExpressionReferencingType {

    private final LiteralToken literalToken;

    private final LocalVariable localVariable;

    public GetLocalNode(LiteralToken literalToken, LocalVariable localVariable) {
        this.literalToken = literalToken;
        this.localVariable = localVariable;
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        setFurtherExpression(newNode);
    }

    @Override
    public Type getType() {
        return localVariable.getTypeRef().getType();
    }

    @Override
    public Type getGenericType() {
        return localVariable.getTypeRef().getGenericType();
    }

    public Type getReturnType() {
        if (furtherExpression != null) {
            return furtherExpression.getReturnType();
        }
        return getType();
    }

    public Type getGenericReturnType() {
        if (furtherExpression != null) {
            return furtherExpression.getGenericReturnType();
        }
        return getGenericType();
    }

    @Override
    public String toString() {
        return literalToken.toString() + (furtherExpression != null ? furtherExpression.toString() : "");
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }

    @Override
    public int getEndCol() {
        return literalToken.getCol() + literalToken.getText().length();
    }

    @Override
    public int getEndRow() {
        return literalToken.getRow();
    }
}
