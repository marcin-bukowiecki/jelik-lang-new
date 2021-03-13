package org.jelik.parser.ast;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.context.NodeContext;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LiteralExpr extends Expression {

    private final LiteralToken literalToken;

    private final NodeContext nodeContext = new NodeContext();

    public LiteralExpr(LiteralToken literalToken) {
        this.literalToken = literalToken;
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getEndCol() {
        return literalToken.getCol() + literalToken.getText().length();
    }

    @Override
    public int getEndRow() {
        return literalToken.getRow();
    }

    public LiteralToken getLiteralToken() {
        return literalToken;
    }

    @Override
    public String toString() {
        return literalToken + (furtherExpression == null ? "" : furtherExpression.toString());
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public void setFurtherExpression(Expression furtherExpression) {
        this.furtherExpression = furtherExpression;
    }

    @Override
    public NodeContext getNodeContext() {
        return nodeContext;
    }

    @Override
    public void setType(Type type) {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public void setGenericType(Type type) {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getReturnType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getGenericReturnType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getGenericType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }
}
