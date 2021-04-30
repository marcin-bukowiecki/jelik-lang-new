package org.jelik.parser.ast;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
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
