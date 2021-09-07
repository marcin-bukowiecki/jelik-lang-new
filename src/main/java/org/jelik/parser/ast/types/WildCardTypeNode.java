package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
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
        this.token = new MulOperator(-1);
    }

    @Override
    public int getStartOffset() {
        return token.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return token.getEndOffset();
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
