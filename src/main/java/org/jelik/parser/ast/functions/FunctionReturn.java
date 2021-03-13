package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionReturn extends ASTNode {

    private final Token arrow;

    private final TypeNode typeNode;

    public FunctionReturn(Token arrow, TypeNode typeNode) {
        this.arrow = arrow;
        this.typeNode = typeNode;
    }

    public Token getArrow() {
        return arrow;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    @Override
    public String toString() {
        return arrow.toString() + " " + typeNode.toString();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
