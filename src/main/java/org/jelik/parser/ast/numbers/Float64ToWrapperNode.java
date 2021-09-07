package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.DoubleWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Float64ToWrapperNode extends TypedExpression {

    private final Expression subject;

    public Float64ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(DoubleWrapperType.INSTANCE);
        this.nodeContext.setGenericType(DoubleWrapperType.INSTANCE);
    }

    @NotNull
    public Expression getSubject() {
        return subject;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "Double.valueOf(" + subject.toString() + ")";
    }
}
