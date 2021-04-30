package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.FloatWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Float32ToWrapperNode extends ExpressionWithType {

    private final Expression subject;

    public Float32ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(FloatWrapperType.INSTANCE);
        this.nodeContext.setGenericType(FloatWrapperType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "Float.valueOf(" + subject.toString() + ")";
    }
}
