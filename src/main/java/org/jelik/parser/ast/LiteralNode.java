package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LiteralNode extends ASTNodeImpl {

    private final LiteralToken literalToken;

    public LiteralNode(LiteralToken literalToken) {
        this.literalToken = literalToken;
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }
}
