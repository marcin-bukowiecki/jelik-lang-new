package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMByteType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int32ToInt8Node extends CastToNode {

    public Int32ToInt8Node(Expression subject) {
        super(subject);
        getNodeContext().setType(JVMByteType.INSTANCE);
        getNodeContext().setGenericType(JVMByteType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
