package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ObjectToInt32Node extends ExpressionWithType {

    private final Expression subject;

    public ObjectToInt32Node(Expression subject) {
        subject.setParent(this);
        this.subject = subject;
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
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
