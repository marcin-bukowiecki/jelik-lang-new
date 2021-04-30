package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.LongWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int64ToWrapperNode extends ExpressionWithType {

    private final Expression subject;

    public Int64ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(LongWrapperType.INSTANCE);
        this.nodeContext.setGenericType(LongWrapperType.INSTANCE);
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
