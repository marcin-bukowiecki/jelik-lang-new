package org.jelik.parser.ast;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base class for Abstract Syntax Tree node
 *
 * @author Marcin Bukowiecki
 */
public abstract class ASTNode {

    public ASTNode parent;

    public abstract void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public Type getPrevType() {
        Objects.requireNonNull(parent, "parent is null for: " + this.getClass().getCanonicalName());
        return parent.getPrevType();
    }

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public boolean isLogical() {
        return false;
    }

    public int getStartCol() {
        return -1;
    }

    public int getStartRow() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public int getEndCol() {
        return -1;
    }

    public int getEndRow() {
        return -1;
    }

    public boolean isZero() {
        return false;
    }
}
