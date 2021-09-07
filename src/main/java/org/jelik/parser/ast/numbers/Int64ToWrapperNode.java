package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.LongWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Int64ToWrapperNode extends TypedExpression {

    private final Expression subject;

    public Int64ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(LongWrapperType.INSTANCE);
        this.nodeContext.setGenericType(LongWrapperType.INSTANCE);
    }

    @NotNull
    public Expression getSubject() {
        return subject;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitInt64ToWrapperNode(this, compilationContext);
    }

    @Override
    public String toString() {
        return "Long.valueOf(" + subject.toString() + ")";
    }
}
