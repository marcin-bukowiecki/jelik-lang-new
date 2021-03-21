package org.jelik.parser.ast;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.DotToken;
import org.jetbrains.annotations.NotNull;

/**
 * Represents foo.bar or foo.bar() etc.
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class DotCallExpr extends ExpressionReferencingType {

    private Expression subject;

    private final DotToken dotToken;

    public DotCallExpr(Expression subject, DotToken dotToken) {
        this.subject = subject;
        this.subject.setParent(this);
        this.dotToken = dotToken;
    }

    @Override
    public int getStartRow() {
        return subject.getStartRow();
    }

    @Override
    public int getStartCol() {
        return subject.getStartRow();
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
        if (subject == oldNode) {
            this.subject = newNode;
        } else {
            setFurtherExpression(newNode);
        }
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitDotCall(this, compilationContext);
    }

    @Override
    public String toString() {
        return subject.toString() + dotToken.toString() + (furtherExpression == null ? "" : furtherExpression.toString());
    }
}
