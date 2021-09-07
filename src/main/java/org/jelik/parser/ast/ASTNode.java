package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.utils.ASTDataKey;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface ASTNode {

    int getStartOffset();

    int getEndOffset();

    @NotNull
    ASTNode getParent();

    void setParent(@NotNull ASTNode parent);

    void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    boolean isZero();

    void setIgnored(boolean ignored);

    <T> void putData(ASTDataKey<T> key, T data);

    <T> T getData(ASTDataKey<T> key);

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
