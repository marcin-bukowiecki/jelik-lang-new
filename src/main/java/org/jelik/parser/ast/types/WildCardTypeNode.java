package org.jelik.parser.ast.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.MulOperator;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class WildCardTypeNode extends TypeNode {

    private final MulOperator token;

    public WildCardTypeNode(MulOperator token) {
        this.token = token;
    }

    public WildCardTypeNode() {
        this.token = new MulOperator(-1, -1);
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
        astVisitor.visitWildCardTypeNode(this, compilationContext);
    }

    @Override
    public Type getType() {
        return nodeContext.getType();
    }

    public String getSymbol() {
        return token.getText();
    }
}
