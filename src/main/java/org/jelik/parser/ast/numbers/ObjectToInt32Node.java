package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ObjectToInt32Node extends ExpressionWithType {

    private final Expression subject;

    public ObjectToInt32Node(Expression subject, Expression further) {
        setFurtherExpression(further);
        subject.setParent(this);
        subject.clearFurtherExpression();
        this.subject = subject;
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "((Integer) " + subject.toString() + ").intValue()";
    }
}
