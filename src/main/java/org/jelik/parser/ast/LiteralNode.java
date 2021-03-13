package org.jelik.parser.ast;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

public class LiteralNode extends ASTNode {

    private final LiteralToken literalToken;

    public LiteralNode(LiteralToken literalToken) {
        this.literalToken = literalToken;
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }
}
