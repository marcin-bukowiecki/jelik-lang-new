package org.jelik.parser.ast.branching;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class IfConditionExpression extends ExpressionReferencingType {

    public IfConditionExpression(Expression subject) {
        setFurtherExpression(subject);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        Objects.requireNonNull(newNode);
        assert furtherExpression == oldNode : "Old noe must be same";
        setFurtherExpression(newNode);
        getNodeContext().setTypeRef(new InferredTypeRef(newNode));
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
