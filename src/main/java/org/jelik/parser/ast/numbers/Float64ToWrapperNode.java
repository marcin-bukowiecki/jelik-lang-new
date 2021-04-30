package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.DoubleWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Float64ToWrapperNode extends ExpressionWithType {

    private final Expression subject;

    public Float64ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(DoubleWrapperType.INSTANCE);
        this.nodeContext.setGenericType(DoubleWrapperType.INSTANCE);
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
