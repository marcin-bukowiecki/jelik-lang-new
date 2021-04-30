package org.jelik.parser.ast.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class SingleTypeNode extends TypeNode {

    private final LiteralToken token;

    public SingleTypeNode(LiteralToken token) {
        this.token = token;
    }

    public SingleTypeNode(String name) {
        this.token = new LiteralToken(name);
    }

    @Override
    public int getStartRow() {
        return token.getRow();
    }

    @Override
    public int getStartCol() {
        return token.getCol();
    }

    @Override
    public int getEndRow() {
        return token.getEndRow();
    }

    @Override
    public int getEndCol() {
        return token.getEndCol();
    }

    public String getText() {
        return token.getText();
    }

    @Override
    public String toString() {
        return token.toString();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitSingleTypeNode(this, compilationContext);
    }

    @Override
    public Type getType() {
        return nodeContext.getType();
    }

    public String getSymbol() {
        return token.getText();
    }
}
