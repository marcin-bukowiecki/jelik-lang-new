package org.jelik.parser.ast;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.common.DupNodeImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface ASTNode {

    int getStartCol();

    int getStartRow();

    int getEndCol();

    int getEndRow();

    @NotNull
    ASTNode getParent();

    void setParent(@NotNull ASTNode parent);

    void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    boolean isZero();

    void setIgnored(boolean ignored);

    boolean isIgnored();

    default @NotNull List<? extends ASTNode> getChildren() {
        return Collections.emptyList();
    }

    default void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    default Expression replaceWithAndReturn(@NotNull Expression oldNode, @NotNull Expression newNode) {
        replaceWith(oldNode, newNode);
        return newNode;
    }
}
