package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Int32ToWrapperNode extends TypedExpression {

    private final Expression subject;

    public Int32ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(IntegerWrapperType.INSTANCE);
        this.nodeContext.setGenericType(IntegerWrapperType.INSTANCE);
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
        return "Integer.valueOf(" + subject.toString() + ")";
    }
}
