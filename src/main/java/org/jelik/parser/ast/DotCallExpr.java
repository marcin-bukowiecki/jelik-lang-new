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

    private final DotToken dotToken;

    public DotCallExpr(DotToken dotToken) {
        this.dotToken = dotToken;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        setFurtherExpression(newNode);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitDotCall(this, compilationContext);
    }

    @Override
    public String toString() {
        return dotToken.toString() + (furtherExpression == null ? "" : furtherExpression.toString());
    }
}
