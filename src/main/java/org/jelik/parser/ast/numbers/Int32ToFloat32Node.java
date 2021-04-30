package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMFloatType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int32ToFloat32Node extends CastToNode {

    public Int32ToFloat32Node(Expression subject) {
        super(subject);
        getNodeContext().setType(JVMFloatType.INSTANCE);
        getNodeContext().setGenericType(JVMFloatType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
