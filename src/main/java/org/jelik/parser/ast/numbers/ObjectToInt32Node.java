package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ObjectToInt32Node extends TypedExpression {

    private final Expression subject;

    public ObjectToInt32Node(Expression subject) {
        subject.setParent(this);
        this.subject = subject;
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
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
        return "((Integer) " + subject.toString() + ").intValue()";
    }
}
