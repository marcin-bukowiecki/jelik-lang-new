package org.jelik.parser.ast.types;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.operators.MulOperator;
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
    public int getStartRow() {
        return typeNode.getStartRow();
    }

    @Override
    public int getStartCol() {
        return typeNode.getStartCol();
    }

    @Override
    public int getEndRow() {
        return parentTypeNode.getEndRow();
    }

    @Override
    public int getEndCol() {
        return parentTypeNode.getEndCol();
    }

    public String getText() {
        return toString();
    }

    @Override
    public String toString() {
        return typeNode.toString() + " " + token.toString() + " " + parentTypeNode.toString();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
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
