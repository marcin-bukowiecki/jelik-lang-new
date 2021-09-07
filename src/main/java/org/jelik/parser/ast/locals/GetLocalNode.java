package org.jelik.parser.ast.locals;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GetLocalNode extends TypedExpression {

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
    public int getStartOffset() {
        return literalToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return literalToken.getEndOffset();
    }
}
