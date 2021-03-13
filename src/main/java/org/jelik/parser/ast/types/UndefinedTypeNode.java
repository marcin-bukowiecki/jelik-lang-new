package org.jelik.parser.ast.types;

import org.jelik.CompilationContext;
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
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public String toString() {
        return "UNDEFINED";
    }

    @Override
    public String getSymbol() {
        throw new UnsupportedOperationException();
    }
}
