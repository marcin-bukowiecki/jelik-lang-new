package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.data.FieldData;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GetFieldNode extends TypedExpression {

    private final LiteralToken literalToken;

    private FieldData fieldData;

    public GetFieldNode(LiteralToken literalToken) {
        this.literalToken = literalToken;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    public Expression getCallingReference() {
        return ((ReferenceExpressionImpl) getParent()).getReference();
    }

    @Override
    public Type getType() {
        return getNodeContext().getType();
    }

    @Override
    public Type getGenericType() {
        return getNodeContext().getGenericType();
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

    public String getName() {
        return literalToken.getText();
    }

    public void setRef(FieldData fieldData) {
        this.fieldData = fieldData;
    }

    public FieldData getRef() {
        return this.fieldData;
    }
}
