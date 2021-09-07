package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeAccessNodeTyped extends TypedExpression {

    private final LiteralToken literalToken;

    public TypeAccessNodeTyped(LiteralToken literalToken, Type type, Type genericType) {
        this.literalToken = literalToken;
        this.nodeContext.setType(type);
        this.nodeContext.setGenericType(genericType);
    }

    @Override
    public int getStartOffset() {
        return literalToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return literalToken.getEndOffset();
    }

    public LiteralToken getLiteralToken() {
        return literalToken;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }
}
