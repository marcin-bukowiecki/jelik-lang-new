package org.jelik.parser.ast.types;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeAccessNode extends ExpressionWithType {

    private final LiteralToken literalToken;

    public TypeAccessNode(LiteralToken literalToken, Type type, Type genericType) {
        this.literalToken = literalToken;
        this.nodeContext.setType(type);
        this.nodeContext.setGenericType(genericType);
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
    public int getEndRow() {
        return furtherExpression == null ? literalToken.getEndRow() : furtherExpression.getEndRow();
    }

    @Override
    public int getEndCol() {
        return furtherExpression == null ? literalToken.getEndCol() : furtherExpression.getEndCol();
    }

    public LiteralToken getLiteralToken() {
        return literalToken;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalToken.toString() + getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}