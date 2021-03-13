package org.jelik.parser.ast.arguments;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

public class Argument extends ASTNode {

    @Getter
    private Expression expression;

    public Argument(Expression expression) {
        this.expression = expression;
        expression.setParent(this);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (oldNode == expression) {
            expression = newNode;
            newNode.parent = this;
        } else {
            throw new IllegalArgumentException("Could not find node for replace: " + oldNode);
        }
    }

    public Type getReturnType() {
        return expression.getReturnType();
    }

    public Type getGenericReturnType() {
        return expression.getGenericReturnType();
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
