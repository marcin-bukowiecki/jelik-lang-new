package org.jelik.parser.ast.arguments;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Argument extends ASTNodeImpl {

    @Getter
    private Expression expression;

    public Argument(@NotNull Expression expression) {
        this.expression = expression;
        expression.setParent(this);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (oldNode == expression) {
            expression = newNode;
            newNode.setParent(this);
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
