package org.jelik.parser.ast.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMVoidType;
import org.jetbrains.annotations.NotNull;

/**
 * For replacing null
 *
 * @author Marcin Bukowiecki
 */
public class VoidTypeNode extends TypeNode {

    public VoidTypeNode() {
        this.nodeContext.setType(JVMVoidType.INSTANCE);
        this.nodeContext.setGenericType(JVMVoidType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public String getSymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEndRow() {
        throw new UnsupportedOperationException();
    }
}
