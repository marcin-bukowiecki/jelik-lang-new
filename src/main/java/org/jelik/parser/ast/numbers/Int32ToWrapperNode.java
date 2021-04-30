package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int32ToWrapperNode extends ExpressionWithType {

    private final Expression subject;

    public Int32ToWrapperNode(Expression subject) {
        this.subject = subject;
        subject.setParent(this);
        this.nodeContext.setType(IntegerWrapperType.INSTANCE);
        this.nodeContext.setGenericType(IntegerWrapperType.INSTANCE);
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
