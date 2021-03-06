package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.ColonToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents:
 *
 * T : Runnable etc.
 *
 * @author Marcin Bukowiecki
 */
public class CovariantTypeNode extends TypeNode {

    private final TypeNode typeNode;

    private final ColonToken token;

    private final TypeNode parentTypeNode;

    public CovariantTypeNode(TypeNode typeNode, ColonToken token, TypeNode parentTypeNode) {
        this.typeNode = typeNode;
        this.token = token;
        this.parentTypeNode = parentTypeNode;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public TypeNode getParentTypeNode() {
        return parentTypeNode;
    }

    @Override
    public int getStartOffset() {
        return typeNode.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return parentTypeNode.getEndOffset();
    }

    public String getText() {
        return toString();
    }

    @Override
    public String toString() {
        return typeNode.toString() + " " + token.toString() + " " + parentTypeNode.toString();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitCovariantTypeNode(this, compilationContext);
    }

    @Override
    public Type getType() {
        return nodeContext.getType();
    }

    public String getSymbol() {
        return typeNode.getSymbol();
    }
}
