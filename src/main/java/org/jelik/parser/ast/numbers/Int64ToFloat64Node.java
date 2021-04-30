package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMDoubleType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int64ToFloat64Node extends CastToNode {

    public Int64ToFloat64Node(Expression subject) {
        super(subject);
        getNodeContext().setType(JVMDoubleType.INSTANCE);
        getNodeContext().setGenericType(JVMDoubleType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
