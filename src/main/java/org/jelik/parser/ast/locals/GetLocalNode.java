package org.jelik.parser.ast.locals;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GetLocalNode extends ExpressionWithType {

    private final LiteralToken literalToken;

    private final LocalVariable localVariable;

    public GetLocalNode(LiteralToken literalToken, LocalVariable localVariable) {
        this.literalToken = literalToken;
        this.localVariable = localVariable;
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public Type getType() {
        return localVariable.getTypeRef().getType();
    }

    @Override
    public Type getGenericType() {
        return localVariable.getTypeRef().getGenericType();
    }

    public Type getReturnType() {
        return getType();
    }

    public Type getGenericReturnType() {
        return getGenericType();
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }

    @Override
    public int getEndCol() {
        return literalToken.getCol() + literalToken.getText().length();
    }

    @Override
    public int getEndRow() {
        return literalToken.getRow();
    }
}
