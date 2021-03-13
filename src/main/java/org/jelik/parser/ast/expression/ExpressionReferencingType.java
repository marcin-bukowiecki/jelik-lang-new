package org.jelik.parser.ast.expression;

import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.context.TypedRefNodeContext;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.types.Type;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for expressions referencing type i.e. {@link org.jelik.parser.ast.locals.ValueDeclaration}
 */
public abstract class ExpressionReferencingType extends Expression {

    protected final TypedRefNodeContext typedRefNodeContext;

    public ExpressionReferencingType() {
        this.typedRefNodeContext = new TypedRefNodeContext();
    }

    public ExpressionReferencingType(TypedRefNodeContext typedRefNodeContext) {
        this.typedRefNodeContext = typedRefNodeContext;
    }

    @Override
    public void setFurtherExpression(@Nullable Expression furtherExpression) {
        super.setFurtherExpression(furtherExpression);
        if (this.furtherExpression != null) {
            typedRefNodeContext.setTypeRef(new InferredTypeRef(furtherExpression));
        }
    }

    @Override
    public TypedRefNodeContext getNodeContext() {
        return typedRefNodeContext;
    }

    @Override
    public Type getType() {
        return this.typedRefNodeContext.getTypeRef().getType();
    }

    @Override
    public Type getGenericType() {
        return this.typedRefNodeContext.getTypeRef().getGenericType();
    }

    @Override
    public Type getReturnType() {
        if (furtherExpression == null) {
            return getType();
        } else {
            return furtherExpression.getReturnType();
        }
    }

    @Override
    public Type getGenericReturnType() {
        if (furtherExpression == null) {
            return getGenericType();
        } else {
            return furtherExpression.getGenericReturnType();
        }
    }

    @Override
    public void setType(Type type) {
        this.typedRefNodeContext.getTypeRef().setType(type);
    }

    @Override
    public void setGenericType(Type type) {
        this.typedRefNodeContext.getTypeRef().setGenericType(type);
    }
}
