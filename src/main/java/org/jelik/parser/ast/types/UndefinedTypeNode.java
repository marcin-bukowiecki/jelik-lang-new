package org.jelik.parser.ast.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * For replacing null
 *
 * @author Marcin Bukowiecki
 */
public class UndefinedTypeNode extends TypeNode {

    public static final UndefinedTypeNode UNDEFINED_TYPE_NODE = new UndefinedTypeNode();

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public String toString() {
        return "UNDEFINED";
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
