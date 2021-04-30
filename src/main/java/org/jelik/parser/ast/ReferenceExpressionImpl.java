package org.jelik.parser.ast;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents foo.bar or foo.bar() etc.
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class ReferenceExpressionImpl extends ASTNodeImpl implements ReferenceExpression {

    private Expression reference;

    private Expression furtherExpression;

    private final Token token;

    public ReferenceExpressionImpl(Expression reference, Token token) {
        this.reference = reference;
        this.reference.setParent(this);
        this.token = token;
    }

    public void setFurtherExpression(Expression furtherExpression) {
        this.furtherExpression = furtherExpression;
        this.furtherExpression.setParent(this);
    }

    public Expression getReference() {
        return reference;
    }

    public Expression getFurtherExpression() {
        return furtherExpression;
    }

    @Override
    public int getStartRow() {
        return reference.getStartRow();
    }

    @Override
    public int getStartCol() {
        return reference.getStartRow();
    }

    @Override
    public int getEndCol() {
        return getFurtherExpression().getEndCol();
    }

    @Override
    public int getEndRow() {
        return getFurtherExpression().getEndRow();
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (reference == oldNode) {
            this.reference = newNode;
        } else {
            this.furtherExpression = newNode;
        }
        newNode.setParent(this);
    }

    @Override
    public Type getReturnType() {
        return furtherExpression.getReturnType();
    }

    @Override
    public Type getGenericReturnType() {
        return furtherExpression.getGenericReturnType();
    }

    @Override
    public Type getType() {
        return reference.getReturnType();
    }

    @Override
    public Type getGenericType() {
        return reference.getGenericReturnType();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitReferenceExpression(this, compilationContext);
    }

    @Override
    public String toString() {
        return reference.toString() +
                token.toString() + (furtherExpression == null ? "" : furtherExpression.toString());
    }
}
