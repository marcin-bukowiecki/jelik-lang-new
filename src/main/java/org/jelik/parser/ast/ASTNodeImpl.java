package org.jelik.parser.ast;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.utils.ASTDataHolder;
import org.jelik.parser.ast.utils.ASTDataKey;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for Abstract Syntax Tree node
 *
 * @author Marcin Bukowiecki
 */
public abstract class ASTNodeImpl implements ASTNode {

    private ASTNode parent = EmptyAstNode.Companion.getINSTANCE();

    protected final ASTDataHolder dataHolder = new ASTDataHolder();

    public abstract void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    public boolean isEmpty() {
        return this instanceof EmptyAstNode;
    }

    @NotNull
    public ASTNode getParent() {
        return parent;
    }

    public void setParent(@NotNull ASTNode parent) {
        this.parent = parent;
    }

    public int getStartCol() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public int getStartRow() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public int getEndCol() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public int getEndRow() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    @Override
    public <T> void putData(ASTDataKey<T> key, T data) {
        dataHolder.putData(key, data);
    }

    @Override
    public <T> T getData(ASTDataKey<T> key) {
        return dataHolder.getData(key);
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public void setIgnored(boolean ignored) {
        this.dataHolder.putData(ASTDataKey.IS_IGNORED, ignored);
    }

    @Override
    public boolean isIgnored() {
        var isIgnored = this.dataHolder.getData(ASTDataKey.IS_IGNORED);
        if (isIgnored == null) return false;
        return isIgnored;
    }
}
