package org.jelik.parser.ast.casts;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class CastObjectToObjectNode extends ExpressionReferencingType {

    private final Type from;

    private final Expression subject;

    private final Type to;

    public CastObjectToObjectNode(Expression subject, Expression further, Type from, Type to) {
        setFurtherExpression(further);
        this.subject = subject;
        subject.setParent(this);
        subject.clearFurtherExpression();
        this.from = from;
        this.to = to;
        typedRefNodeContext.setTypeRef(new InferredTypeRef(subject));
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "((" + to.toString() + ") " + subject.toString() + ")" + getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
