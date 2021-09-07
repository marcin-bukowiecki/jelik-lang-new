package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeVariableNode extends TypeNode {

    private final TypeNode typeNode;

    public TypeVariableNode(TypeNode typeNode) {
        this.typeNode = typeNode;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public Type getType() {
        return typeNode.getType();
    }

    @Override
    public String getSymbol() {
        return typeNode.getSymbol();
    }

    @Override
    public int getEndOffset() {
        return typeNode.getEndOffset();
    }
}
